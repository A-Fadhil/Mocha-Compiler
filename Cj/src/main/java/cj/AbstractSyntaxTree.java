package cj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class AbstractSyntaxTree
{
    public static AbstractSyntaxTree tree;
    Parser parser;

    HashMap<String, Integer>        type_size = new HashMap<>();
    HashMap<String, Integer>        method_ids = new HashMap<>();
    HashMap<String, Integer>        GlobalTypes = new HashMap<>();
    HashMap<String, String>         GlobalTypeOfs = new HashMap<>();
    HashMap<String, Type>           GlobalClasses = new HashMap<String, Type>();

    HashMap<Integer, OpCode>        op_code = new HashMap<Integer, OpCode>();

    HashMap<String, Type> types = new HashMap<>();
    HashMap<String, Method> methods = new HashMap<>();
    Method main_method;

    public AbstractSyntaxTree(Parser parser)
    {
        this.parser = parser;
        tree        = this;
        type_size.put("byte", 1);
        type_size.put("char", 1);
        type_size.put("short", 2);
        type_size.put("u_short", 2);
        type_size.put("int", 4);
        type_size.put("u_int", 4);
        type_size.put("long", 8);
        type_size.put("u_long", 8);
        type_size.put("String", 4);
        type_size.put("unicode_c", 2);
        type_size.put("float", 8);
        type_size.put("lowp_f", 4);
    }

    private void u_enter(TokenStream stream)
    {
        while(stream.pvNext() != null)
        {
            Token next = stream.next();

            if(next.isIdentifier() && next.isUnsigned())
            {
                if(next.value.equals("int")) next.value = "u_int";
                if(next.value.equals("long")) next.value = "u_long";
            } else u_enter(next.TokenStream().nullable());
        }
    }

    public void enter(TokenStream stream)
    {
        stream.nullable();
        u_enter(stream);
        stream.rewind();

        //calculate sizeof() structs and classes
        while (stream.pvNext() != null)
        {
            Token next = stream.next();

            if (next.type.equals("struct"))
            {
                type_size.put(next.value, calcSize(next.child.get(0).TokenStream(), next, stream.force));
                System.out.println("sizeof(" + next.value + ") = " + type_size.get(next.value));
            } else if(next.type.equals("class"))
            {
                type_size.put(next.value, calcSize(next.child.get(0).TokenStream(), next, stream.force));
                System.out.println("sizeof(" + next.value + ") = " + type_size.get(next.value));
            } else if(next.type.equals("field"))
            {
                System.err.println("error in 'program' at line " + next.line + " : " + next.begin() + " (declaration outside of class-type).");
                System.exit(0);
            }
        }


        //add Parent* this to method_ids, oh OOP!
        stream.rewind();
        while (stream.pvNext() != null)
        {
            Token next = stream.next();

            if (next.type.equals("struct"))
            {
                setupMemberMethods(next.child.get(0).TokenStream(), next, stream.force);
            } else if(next.type.equals("class"))
            {
                setupMemberMethods(next.child.get(0).TokenStream(), next, stream.force);
            } else if(next.type.equals("field"))
            {
            }
        }

        stream.rewind();

        while(stream.pvNext() != null)
        {
            Token next = stream.next();

            if(next.isClassType())
            {
                types.put(next.value, new Type(next));
            } else if(next.isMethod())
            {
                methods.put(next.value, new Method(next));
            }
        }
    }

    private void registerMethod(Token method)
    {
        String name = method.stubValue();

        if(method_ids.containsKey(name))
        {
            System.err.println("method '" + name + "' at line: " + method.line + " : " + method.begin() + " already exists.");
            System.exit(0);
        } else {
            ArrayList <Byte> op_code;
        }
    }

    protected REST createField(boolean isMethod, OpCode code, Token next, Token value)
    {
        String REFERENCE_TYPE = next.child.get(0).type;
        String type = next.child.get(0).value;
        String name = next.child.get(1).value;

        boolean hasValue = value != null;
        boolean isInput  = hasValue ? value.type.equals("input") : false;

        Number number    = null;
        if(hasValue && isInput)
        {
            switch (value.value)
            {
                case "int": number = Long.parseLong(value.child.get(0).value);
            }
        }

        boolean unsigned = value.child.get(0).unsigned;

        boolean isConstructorCall = next.child.get(1).type.equals("call");

        if(REFERENCE_TYPE.equals("reference"))
        {
        } else if(REFERENCE_TYPE.equals("pointer"))
        {
        } else {
            switch (type)
            {
                case "byte": code.pshByte(number); return REST.BYTE;
                case "char": code.pshChar(number); return REST.UBYTE;
                case "short": code.pshShort(number); return REST.INT;
                case "u_short": code.pshUnsignedShort(number); return REST.UINT;
                case "u_int": code.uint_ld(number); return REST.UINT;
                case "int": code.pshInt(number); return REST.INT;
                case "long": code.pshLong(number); return REST.LONG;
                case "unicode_c": code.pshChar(number); return REST.INT;
                case "float": code.pshFloat(number); return REST.DOUBLE;
                case "lowp_f": code.pshLowp_f(number); return REST.DOUBLE;
                case "string": code.pshString(value.child.get(0).value); return REST.UINT;
                case "bool": code.pshBool(null); return REST.UBYTE;
                default:
                    Method constructor = null;
                    Type t = types.get(type);
                    if(t == null)
                    {
                        System.err.println("undefined type '" + type + "' at line: " + next.line + " : " + next.begin());
                        System.exit(0);
                    }

                    constructor = next.child.get(1).type.equals("call") ? t.getConstructor(next.child.get(1)) : t.initializer;
                    code.pshReference(type_size.get(type), constructor);

                    return REST.UINT;
            }
        }

        return REST.UINT;
    }

    protected void createOpCode(boolean isMethod, Container container, Token token)
    {
        TokenStream stream = token.TokenStream();
        if(isMethod && token.child.size() > 0) stream = token.child.get(0).TokenStream();
        stream.nullable();

        while(stream.pvNext() != null)
        {
            Token next = stream.next();

            cycleByteCode(isMethod, container, next);
        }
    }

    protected void createOpCode(boolean isMethod, OpCode code, Token token)
    {
        TokenStream stream = token.TokenStream();
        if(isMethod && token.child.size() > 0) stream = token.child.get(0).TokenStream();
            stream.nullable();

            while(stream.pvNext() != null)
            {
                Token next = stream.next();

                cycleByteCode(isMethod, code, next);
            }
    }


    protected void cycleByteCode(boolean isMethod, Container container, Token next)
    {
        if(next.type.equals("set"))
        {
            container.setField(next);
//            if(next.child.get(0).type.equals("field"))
//            {
//                createField(false, code, next.child.get(0), next.child.get(1));
////                cycleByteCode(false, code, next.child.get(1));
//            }
//            else createOpCode(false, code, next);
//
//            Token lastToken = next.child.get(1);
//
//            if(lastToken.type.equals("input") || (lastToken.type.equals("call") && types.containsKey(lastToken.value)) || types.containsKey(lastToken.value) || (lastToken.type.equals("new") || types.containsKey(lastToken.value)))
//                code.ref_set_value();
//            else code.ref_set();
        }
        else if(next.isField())
        {
            container.addField(next);
//            createField(isMethod, code, next, null);
        } else if(next.type.equals("call"))
        {
//            if(types.containsKey(next.value))
//            {
//                Type type = types.get(next.value);
//
//                Method constructor = type.getConstructor(next);
//                code.inline(constructor);
//            }
//            else if(next.value.equals("printn") || next.value.equals("println"))
//            {
//                createOpCode(false, code, next);
////                        toString(isMethod, code, next.child.get(0), true);
//                code.println();
//            } else if(next.value.equals("print"))
//            {
//                createOpCode(isMethod, code, next);
////                        code.print();
//            } else if(next.value.equals("sizeof"))
//            {
//                if(type_size.containsKey(next.child.get(0).child.get(0).value)) code.uint_ld(type_size.get(next.child.get(0).child.get(0).value));
//                else code.uint_ld(next.child.get(0).child.get(0).size);
//            }
        } //else if(next.value.equals("string")) code.pshString(next.child.get(0).value);//toString(isMethod, code, next.child.get(0), true);
//        else if(next.type.equals("integer")) code.uint_ld(Integer.valueOf(next.value));
//        else if(next.type.equals("parenthesis")) createOpCode(isMethod, code, next);
//        else if(next.type.equals("identifier")) code.get(next.value);
//        else if(types.containsKey(next.value))  code.inline(types.get(next.value).initializer);
//        else createOpCode(isMethod, code, next);
    }

    enum REST {
        INT, UINT, DOUBLE, LONG, ULONG, BYTE, UBYTE;
    }

    protected void cycleByteCode(boolean is, OpCode c, Token next)
    {
        this.cycleByteCode(is, c, next, REST.INT);
    }

    Stack<REST> typem = new Stack<>();

    protected void cycleByteCode(boolean isMethod, OpCode code, Token next, REST r)
    {
        if(next.type.equals("set"))
        {
            if(next.child.get(0).type.equals("field"))
            {
                typem.push(createField(false, code, next.child.get(0), next.child.get(1)));
//                cycleByteCode(false, code, next.child.get(1));
            }
            else createOpCode(false, code, next);

            if(next.child.size() > 1)
            cycleByteCode(false, code, next.child.get(1), typem.pop());

            Token lastToken = next.child.get(1);

            if(lastToken.type.equals("input") || (lastToken.type.equals("call") && types.containsKey(lastToken.value)) || types.containsKey(lastToken.value) || (lastToken.type.equals("new") || types.containsKey(lastToken.value)))
            code.ref_set_value();
            else code.ref_set();
        }
        else if(next.isField())
        {
            createField(isMethod, code, next, null);
        } else if(next.type.equals("call"))
        {
            if(types.containsKey(next.value))
            {
                Type type = types.get(next.value);

                Method constructor = type.getConstructor(next);
                code.inline(constructor);
            }
            else if(next.value.equals("printn") || next.value.equals("println"))
            {
                createOpCode(false, code, next);
//                        toString(isMethod, code, next.child.get(0), true);
                code.println();
            } else if(next.value.equals("print"))
            {
                createOpCode(isMethod, code, next);
//                        code.print();
            } else if(next.value.equals("sizeof"))
            {
                if(type_size.containsKey(next.child.get(0).child.get(0).value)) code.uint_ld(type_size.get(next.child.get(0).child.get(0).value));
                else code.uint_ld(next.child.get(0).child.get(0).size);
            }
        } else if(next.value.equals("string")) code.pshString(next.child.get(0).value);//toString(isMethod, code, next.child.get(0), true);
        else if(next.type.equals("integer")) code.uint_ld(Integer.valueOf(next.value));
        else if(next.type.equals("parenthesis")) createOpCode(isMethod, code, next);
        else if(next.type.equals("identifier")) code.get(next.value);
        else if(types.containsKey(next.value))  code.inline(types.get(next.value).initializer);
        else if(next.type.equals("input"))
        {
            String type = next.value;
            Number number = null;

            switch (type)
            {
//                case "byte":
//                    code.pshByte(number);
//                    return REST.BYTE;
//                case "char":
//                    code.pshChar(number);
//                    return REST.UBYTE;
//                case "short":
//                    code.pshShort(number);
//                    return REST.INT;
//                case "u_short":
//                    code.pshUnsignedShort(number);
//                    return REST.UINT;
//                case "u_int":
                case "int":
                    code.push_i(Integer.parseInt(next.child.get(0).value)); break;
                case "long":
                    code.pshLong(number); break;
                case "unicode_c":
                    code.pshChar(number); break;
                case "float":
                    code.pshFloat(number); break;
                case "lowp_f":
                    code.pshLowp_f(number); break;
                case "string": break;
                case "bool":
                    code.pshBool(null); break;
            }
        }
        else if(next.type.equals("math"))
        {
            for(Token token : next)
            {
                Token op = token;
                createOpCode(false, code, op);

                if(op.value.equals("mul"))
                {
                    switch (r)
                    {
                        case INT:
                            code.imul();
                            break;
                        case UINT:
                            code.uimul();
                            break;
                    }
                }

                if(op.value.equals("add"))
                {
                    switch (r)
                    {
                        case INT:
                            code.iadd();
                            break;
                        case UINT:
                            code.uiadd();
                            break;
                    }
                }
            }
        }
        else createOpCode(isMethod, code, next);
    }

    private static void println(Token print)
    {
    }

    private static void toString(boolean isMethod, OpCode code, Token token_, boolean addEndLine)
    {
        TokenStream stream = token_.TokenStream();
        stream.nullable();

        while(stream.pvNext() != null)
        {
            Token token = stream.next();

            if(token.type.equals("input"))
            {
                if(token.value.equals("string"))
                {
                    code.pshString(token.child.get(0).value);
                }
            }
        }
    }

    private void setupMemberMethods(TokenStream stream, Token parent, Token root)
    {
        stream.nullable();

        while(stream.pvNext() != null)
        {
            Token next = stream.next();

            if(next.type.equals("method"))
            {
                int size = 0;
                if(!next.isConstructor(parent.value) && !next.isStatic())
                {
                    Token parenthesis = next.child.get(1);
                    ArrayList<Token> nchild = new ArrayList<>();
                    Token field = new Token().l("field", "field");
                    field.add(new Token().l("pointer", parent.value));
                    field.add(new Token().l("identifier", next.isNativeMethod() ? "self" : "this"));

                    nchild.add(0, field);
                    nchild.addAll(parenthesis.child);
                    parenthesis.child.clear();
                    parenthesis.child.addAll(nchild);

                    for(Token feld : parenthesis)
                    {
                        if(!feld.type.equals("seperator"))
                        {
                            next.addField(feld, size);
                            size = size + sizeof(feld.child.get(0).value + (feld.child.get(0).type.equals("pointer") ? "*" : ""));
                        }
                    }

                    next.size = size;
                } else
                {
                    Token parenthesis = next.child.get(1);
                    for(Token feld : parenthesis)
                    {
                        if(!feld.type.equals("seperator"))
                        {
                            next.addField(feld, size);
                            size = size + sizeof(feld.child.get(0).value + (feld.child.get(0).type.equals("pointer") ? "*" : ""));
                        }
                    }

                    next.size = size;
                }

                registerMethod(next);
            }
        }
    }

    private int sizeof(String type)
    {
        if(type.endsWith("*")) return 4;
        return type_size.get(type);
    }

    private int calcSize(TokenStream stream, Token parent, Token root)
    {
        int size = 0;
        stream.nullable();

        while(stream.pvNext() != null)
        {
            Token next = stream.next();

            if(next.type.equals("field"))
            {
                if(!next.isStatic())
                {
                    Token type = next.child.get(0);

                    parent.addField(next, size);

                    if(type.type.equals("pointer")) size = size + 4;
                    else if(type.type.equals("identifier") && type.value.equals(parent.value))
                    {
                        System.err.println("error in '" + parent.type + "' '" + parent.value + "' at line " + type.line + " : " + type.begin() + " (circular field definition).");

                        System.err.println("--structs can't have non-pointer children of the same type.");
                        System.err.println("--circular field definitions cause an infinite loop.");
                        System.exit(0);
                    } else {
//                    System.out.println(type.value + " " + type_size.get(type.value));
                        if(type_size.get(type.value) == null)
                            type_size.put(type.value, calcSize(type.value, root, parent.value));
                        size = size + type_size.get(type.value);
                    }
                } else {
                    GlobalTypes.put(next.child.get(1).value, GlobalTypes.size());
                    GlobalTypeOfs.put(next.child.get(1).value, (next.child.get(0).type.equals("pointer") ? next.child.get(0).value + "*" : next.child.get(0).value));
                }
            } else if(next.type.equals("method"))
            {
//                if(!next.isNativeMethod() && !next.isConstructor(parent.value) && !next.isStatic())
//                {
//                    Token parenthesis = next.child.get(1);
//                    ArrayList<Token> nchild = new ArrayList<>();
//                    Token field = new Token().l("field", "field");
//                    field.add(new Token().l("pointer", parent.value));
//                    field.add(new Token().l("identifier", "this"));
//
//                    nchild.add(0, field);
//                    nchild.addAll(parenthesis.child);
//                    parenthesis.child.clear();
//                    parenthesis.child.addAll(nchild);
//
//                    for(Token feld : parenthesis)
//                    {
//                        next.addField(feld, 0);
//                    }
////                    Parser.listNodes(parenthesis, 0);
//                }
            }
        }

        parent.size = size;

        return size;
    }

    private int calcSize(String name, Token root, String caller)
    {
        int size = 0;
        TokenStream rootStream = root.TokenStream();
        rootStream.nullable();

        while(rootStream.pvNext() != null)
        {
            Token parent = rootStream.next();

            if((parent.type.equals("class") || parent.type.equals("struct")) && parent.value.equals(name))
            {
                TokenStream stream = parent.child.get(0).TokenStream();
                stream.nullable();

                while(stream.pvNext() != null)
                {
                    Token next = stream.next();

                    if(next.type.equals("field"))
                    {
                        if(!next.isStatic())
                        {
                            Token type = next.child.get(0);

                            if (type.type.equals("pointer")) size = size + 4;
                            else if (type.type.equals("identifier") && type.value.equals(parent.value))
                            {
                                System.err.println("error in '" + parent.type + "' '" + parent.value + "' at line " + type.line + " : " + type.begin() + " (circular field definition).");

                                System.err.println("--structs can't have non-pointer children of the same type.");
                                System.err.println("--circular field definitions cause an infinite loop.");
                                System.exit(0);
                            } else
                            {
                                if (type_size.get(type.value) == null)
                                {
                                    if (type.value.equals(caller))
                                    {
                                        System.err.println("error in '" + parent.type + "' '" + parent.value + "' at line " + type.line + " : " + type.begin() + " (circular field definition).");

                                        System.err.println("--structs can't have non-pointer children of the same type.");
                                        System.err.println("--circular field definitions cause an infinite loop.");
                                        System.exit(0);
                                    }
                                    type_size.put(type.value, calcSize(type.value, root, parent.value));
                                } else
                                    size = size + type_size.get(type.value);
                            }
                        } else {
                            GlobalTypes.put(next.child.get(1).value, GlobalTypes.size());
                            GlobalTypeOfs.put(next.child.get(1).value, (next.child.get(0).type.equals("pointer") ? next.child.get(0).value + "*" : next.child.get(0).value));
                        }
                    }
                }
            };
        }

        return size;
    }

    public String generateNativeStub()
    {
        StringBuilder register = new StringBuilder();
        return generateNativeStub(parser.root, register) + "\n\n\n" + register.toString();
    }

    private String generateNativeStub(Token token, StringBuilder register)
    {
        String stub = "";

        if(token.isNativeMethod())
        {
            TokenStream stream = token.child.get(0).TokenStream();
            stream.nullable();

            String argList = "";
            Token  list[] = new Token[stream.capacity()];
            Token blist[] = new Token[stream.capacity()];

            while(stream.pvNext() != null)
            {
                list[stream.index] = stream.pvNext().child.get(0);
                blist[stream.index] = stream.pvNext().child.get(1);

                argList = argList + stream.next().asField() + ", ";
            }

            String body = "";

            for(int i = list.length - 1; i > -1; i --) body = body + "\t\t" + list[i].nativeValue() + " " + blist[i].value + " = " + "program->stack." + list[i].nativePopCall() + ";\n";

            String returnType = token.child.get(token.child.size() - 1).value;//.child.get(0).value;
            String returnName = returnType;

            switch (returnType)
            {
                case "void": break;

                case "byte":        returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "char":        returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "short":       returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "u_short":     returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "int":         returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "u_int":       returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "long":        returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "u_long":      returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "unicode_c":   returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;
                case "String":      returnType = "m" + returnType; body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n"; break;

                default: returnType = "VM::MochaObject";
                    body = body + "\t\t/*return " + returnName + "*/\n\t\tprogram->stack.pushVoid();\n";
                    break;
            }

            argList = "VM::MochaProgram* program, " + argList;

//            stub = stub + returnType + " " + "NativeFunction" + token.stubValue() + " (" + argList.substring(0, argList.length() - 2) + ")\n{\n" + body.substring(0, body.length() - 1) + "\n}\n";
            stub = stub + "struct " + "NativeFunction" + token.stubValue() + " : VM::MochaMethod\n{\n" + "\tvirtual inline void call(VM::MochaProgram* program)\n\t{\n" + body.substring(0, body.length() - 1) + "\n\t}\n};\n";

            register.append("VM::registerNativeFunction(&" + "NativeFunction" + token.stubValue() + "(" + "\"NativeFunction" + token.stubValue() + "\"));\n");
        }
        else for (Token t : token.child) stub = stub + generateNativeStub(t, register);

        return stub;
    }

    public ArrayList<Byte> generateObjectFile()
    {
        enter(parser.root.TokenStream());

        return null;
    }
}