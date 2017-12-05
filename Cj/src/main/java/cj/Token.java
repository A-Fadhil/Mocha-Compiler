package cj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Token implements Iterable<Token>
{
    int begin, end, line;
    Token parent;
    public String type;
    boolean isBlock;
    boolean isDaughterBlock;
    public String value;
    public List<Token> child;
    public boolean nativeMethod;
    public boolean method;
    public boolean isPrivate;
    public boolean isProtected;
    private boolean isPublic;
    public OpCode opcode;

    public TokenStream TokenStream()
    {
        return new TokenStream(this);
    }
    public int begin()
    {
        return end;// - value.length();
    }

    public boolean isCloesed()
    {
        return value.equals("closed") || type.equals("body") || type.equals("statement");
    }

    public Token(String type, String value)
    {
        this.type = type;
        this.value= value;
        this.child= new ArrayList<>();
    }

    public Token l(String type, String value)
    {
        this.type = type;
        this.value= value;

        return this;
    }

    public Token()
    {
        type = "";
        value = "";
        child= new ArrayList<>();
    }

    public Token getParentBlock()
    {
        Token p = parent;

        while(p != null)
        {
            if(p.isBlock) return p;
            p = p.parent;
        }

        return null;
    }

    public boolean inStruct()
    {
        Token p = parent;

        while(p != null)
        {
            if(p.type.equals("struct")) return true;
            p = p.parent;
        }

        return false;
    }

    public boolean inClass()
    {
        Token p = parent;

        while(p != null)
        {
            if(p.type.equals("class")) return true;
            p = p.parent;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return type + " " + value;
    }

    public Token add(Token stringNode)
    {
        child.add(stringNode);
        stringNode.parent = this;

        return this;
    }

    HashMap<String, String> field_type = new HashMap<>();
    HashMap<String, Integer> field_offset = new HashMap<>();
    HashMap<String, Token> field_value = new HashMap<>();
    int size = 0;

    public void addField(Token field, int offset)
    {
//        field_type.put(field.child.get(1).value, field.child.get(0).value + (field.child.get(0).type.equals("pointer") ? "*" : ""));
        field_type.put(field.child.get(1).value, field.child.get(0).value + (field.child.get(0).type.equals("pointer") ? "*" : (field.child.get(0).type.equals("reference") ? "&" : "")));
        field_offset.put(field.child.get(1).value, offset);
    }

    public void addArgument(Token field, int offset)
    {
        field_type.put("arg_" + field.child.get(1).value, field.child.get(0).value + (field.child.get(0).type.equals("pointer") ? "*" : (field.child.get(0).type.equals("reference") ? "&" : "")));
        field_offset.put("arg_" + field.child.get(1).value, offset);
    }

    public int get(String name)
    {
        if(isMethod())
        {
            //object is passed as argument
            if(field_offset.containsKey("arg_" + name)) return field_offset.get("arg_" + name);
            else if(field_offset.containsKey(name)) return field_offset.get(name);
        } else
            if(field_offset.containsKey(name)) return field_offset.get(name);

        System.err.println(name + "does not exist in " + (isMethod() ? "method " : "type ") + value);
        System.exit(0);
        return 0;
    }

//    public Token get(String name)
//    {
//        if(isMethod())
//        {
//            //object is passed as argument
//            if(field_offset.containsKey("arg_" + name)) return field_offset.get("arg_" + name);
//            else if(field_offset.containsKey(name)) return field_offset.get(name);
//        } else
//            if(field_offset.containsKey(name)) return field_offset.get(name);
//
//        System.err.println(name + "does not exist in " + (isMethod() ? "method " : "type ") + value);
//        System.exit(0);
//        return 0;
//    }

    public boolean isMethod()
    {
        return method;
    }

    public boolean isNativeMethod()
    {
        return nativeMethod;
    }

    boolean isstatic;

    public boolean isConstructor(String name)
    {
        return value.equals(name);
    }

    public boolean isStatic() { return isstatic; }

    public String asField()
    {
        return child.get(0).value + " " + child.get(1).value;
    }

    public String asNativeField()
    {
        String type = child.get(0).value;
        switch (type)
        {
            case "void": break;
            case "byte": type = "m" + type; break;
            case "char": type = "m" + type; break;
            case "short": type = "m" + type; break;
            case "u_short": type = "m" + type; break;
            case "int": type = "m" + type; break;
            case "u_int": type = "m" + type; break;
            case "long": type = "m" + type; break;
            case "u_long": type = "m" + type; break;
            case "unicode_c": type = "m" + type; break;
            case "String": type = "m" + type; break;
            case "float": type         = "m" + type; break;
            case "lowp_f": type         = "m" + type; break;

            default: type = "VM::MochaObject"; break;
        }
        return type + " " + child.get(1).value;
    }

    public String stubValue()
    {
        String stub = value;
        if(isMethod())
        {
            String args = "";
            if(child.size() > 0) for(Token token : child.get(1).child) if(token.type.equals("seperator")) args += ", "; else args += token.child.get(0).value;

            if(args.endsWith(", ")) args = args.substring(0, args.length() - 2);
            stub = stub + "(" + args + ")";
        }
        Token p = parent;
        while(p != null)
        {
            if(p.value.equals("open") && p.type.equals("body")) p = p.parent;
            if(p == null) break;
            stub = p.value + "_" + stub;
            p = p.parent;
        }

        return stub;
    }

    public String nativePopCall()
    {
        String type = value;

        switch (type)
        {
            case "void": break;
            case "byte": type           = "popByte()"; break;
            case "char": type           = "popChar()"; break;
            case "short": type          = "popShort()"; break;
            case "u_short": type        = "popUShort()"; break;
            case "int": type            = "popInt()"; break;
            case "u_int": type          = "popUInt()"; break;
            case "long": type           = "popLong()"; break;
            case "u_long": type         = "popULong()"; break;
            case "unicode_c": type      = "popUnicodeChar()"; break;
            case "String": type         = "popString()"; break;
            case "float": type         = "popFloat()"; break;
            case "lowp_f": type         = "popLowp_f()"; break;

            default: type = "popReference()"; break;
        }

        return type;
    }

    public String nativeValue()
    {
        String type = value;
        switch (type)
        {
            case "void": break;
            case "byte": type = "m" + type; break;
            case "char": type = "m" + type; break;
            case "short": type = "m" + type; break;
            case "u_short": type = "m" + type; break;
            case "int": type = "m" + type; break;
            case "u_int": type = "m" + type; break;
            case "long": type = "m" + type; break;
            case "u_long": type = "m" + type; break;
            case "unicode_c": type = "m" + type; break;
            case "String": type = "m" + type; break;
            case "float": type         = "m" + type; break;
            case "lowp_f": type         = "m" + type; break;

            default: type = "VM::MochaObject"; break;
        }

        return type;
    }

    boolean virtul;
    boolean inline;
    boolean isVolatile;
    boolean isConst;
    boolean isFinal;
    boolean isCollectable;
    boolean isUnsafe      = false;
    boolean unsigned      = false;
    boolean explicit      = false;
    boolean low_p         = false;

    public void setKeyWord(Token keyWord)
    {
        if(keyWord.value.equals("static")) isstatic = true;
        if(keyWord.value.equals("virtual")) virtul = true;
        if(keyWord.value.equals("inline")) inline = true;
        if(keyWord.value.equals("native")) nativeMethod = true;
        if(keyWord.value.equals("public")) isPublic = true;
        if(keyWord.value.equals("private")) isPrivate = true;
        if(keyWord.value.equals("protected")) isProtected = true;
        if(keyWord.value.equals("volatile")) isVolatile = true;
        if(keyWord.value.equals("const")) isConst = true;
        if(keyWord.value.equals("final")) isFinal = true;
        if(keyWord.value.equals("collectable")) isCollectable = true;
        if(keyWord.value.equals("unsafe")) isUnsafe = true;
        if(keyWord.value.equals("unsigned")) unsigned = true;
        if(keyWord.value.equals("explicit")) explicit = true;
        if(keyWord.value.equals("low_p")) low_p = true;

        if(unsigned)
        {
            System.out.println(this);
            System.out.println("FUCK");
        }
    }

    public boolean isVirtual() { return virtul; }

    public boolean isInline() { return inline; }

    @Override
    public Iterator<Token> iterator()
    {
        return child.iterator();
    }

    public boolean isIdentifier()
    {
        return type.equals("identifier");
    }

    public boolean isUnsigned()
    {
        return unsigned;
    }

    public boolean isField()
    {
        return type.equals("field");
    }

    public boolean isClassType()
    {
        return type.equals("class") || type.equals("struct") || type.equals("union") || type.equals("primitive");
    }

    Method m;

    public Method asMethod()
    {
        return m;
    }
}