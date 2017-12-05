package cj;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Parser
{
    static final String __KEYWORDS__[] = {"struct", "class", "public", "private", "protected", "volatile", "inline", "void", "delete", "static", "const", "final", "return", "new", "native", "virtual", "class", "primitive", "explicit", "const", "collect", "final", "safe", "unsafe", "unsigned"};
    HashMap<String, String> defines = new HashMap<>();

    private boolean matchKeyword(String string)
    {
        for (String string1 : __KEYWORDS__) if (string.equals(string1)) return true;
        return false;
    }

    private Token stringNode(Token string)
    {
        Token stringNode = new Token("string", string.value.substring(1, string.value.length() - 1));
        Token input = new Token("input", "string");
        input.size = 4;
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    private Token charNode(Token string)
    {
        Token stringNode = new Token("char", string.value.substring(1, string.value.length() - 1));
        Token input = new Token("input", "char");
        input.size  = 1;
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    private Token doubleNode(Token string)
    {
//        Token stringNode = new Token("double", string.replaceAll("d|D", ""));
        Token stringNode = new Token("double", string.value.replaceAll("d|D", ""));
        Token input = new Token("input", "double");
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    private Token floatNode(Token string)
    {
//        Token stringNode = new Token("float", string.replaceAll("f|F", ""));
        Token stringNode = new Token("float", string.value.replaceAll("f|F", ""));
        Token input = new Token("input", "float");
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    private Token intNode(Token string)
    {
//        Token stringNode = new Token("int", string);
        Token stringNode = new Token("int", string.value);
        Token input = new Token("input", "int");
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    private Token longNode(Token string)
    {
//        Token stringNode = new Token("long", string.replaceAll("l|L", ""));
        Token stringNode = new Token("long", string.value.replaceAll("l|L", ""));
        Token input = new Token("input", "long");
        input.end = string.end + 2;
        stringNode.end = string.end + 2;
        input.line = string.line + 2;
        stringNode.line = string.line + 2;

        return input.add(stringNode);
    }

    HashMap<String, String> types = new HashMap<>();

    public static Token NULL;

    public Parser(Lexer lexer) throws Exception {this(lexer, true);}
    public Parser(Lexer lexer, boolean all) throws Exception
    {
        NULL = new Token("empty", "empty");
        Token root = new Token("root", "");
        ArrayList<Token> nodes = new ArrayList<>();

        for (int i = 0; i < lexer.mTokens.size(); i++)
        {
            Token token = lexer.mTokens.get(i);
            String string = token.value;

            if (matchKeyword(string))
            {
                token.type = "keyword";//nodes.add(new Token("keyword", string));
                nodes.add(token);
            } else if (string.equals("["))
            {
                token.type = "keyword";//nodes.add(new Token("bracket", "open"));
                nodes.add(token);
            } else if (string.equals("]"))
            {
                token.type = "keyword";//nodes.add(new Token("bracket", "closed"));
                nodes.add(token);
            } else if (string.matches("([A-z]+(\\d?\\_?\\$?)+)"))
            {
                token.type = "identifier";//nodes.add(new Token("identifier", string));
                nodes.add(token);
            } else if (string.startsWith("\"") & string.endsWith("\""))
            {
//                token.type = "string";//nodes.add(stringNode(string));
                nodes.add(stringNode(token));
            } else if (string.startsWith("\'") & string.endsWith("\'"))
            {
//                token.type = "char";//nodes.add(charNode(string));
                nodes.add(charNode(token));
            } else if (string.matches("\\-?(\\d+\\.)|(\\d*\\.\\d+)(d|D)?"))
            {
//                token.type = "keyword";//nodes.add(doubleNode(string));
                nodes.add(doubleNode(token));
            } else if (string.matches("\\-?(\\d+\\.)|(\\d*\\.\\d+)(f|F)?"))
            {
//                token.type = "keyword";//nodes.add(floatNode(string));
                nodes.add(floatNode(token));
            } else if (string.matches("\\-?(\\d+)(l|L)"))
            {
//                token.type = "keyword";//nodes.add(longNode(string));
                nodes.add(longNode(token));
            } else if (string.matches("\\-?(\\d+)"))
            {
//                token.type = "keyword";//nodes.add(intNode(string));
                nodes.add(intNode(token));
            } else if (string.equals("&") | string.equals("and"))
            {
                token.type = "operation";//nodes.add(new Token("operation", "and"));
                token.value = "and";//nodes.add(new Token("operation", "and"));
                nodes.add(token);
            } else if (string.equals("|") | string.equals("or"))
            {
                token.type = "operation";//nodes.add(new Token("operation", "or"));
                token.value = "or";//nodes.add(new Token("operation", "or"));
                nodes.add(token);
            } else if (string.equals("+"))
            {
                token.l("operation", "add");//nodes.add(new Token("operation", "add"));
                nodes.add(token);
            } else if (string.equals("-"))
            {
                token.l("operation", "sub");//nodes.add(new Token("operation", "sub"));
                nodes.add(token);
            } else if (string.equals("*"))
            {
                token.l("operation", "mul");//nodes.add(new Token("operation", "mul"));
                nodes.add(token);
            } else if (string.equals("/"))
            {
                token.l("operation", "div");//nodes.add(new Token("operation", "div"));
                nodes.add(token);
            } else if (string.equals("%"))
            {
                token.l("operation", "mod");//nodes.add(new Token("operation", "mod"));
                nodes.add(token);
            } else if (string.equals("!"))
            {
                token.l("not", "not");//nodes.add(new Token("not", "not"));
                nodes.add(token);
            } else if (string.equals("^"))
            {
                token.l("operation", "pow");//nodes.add(new Token("operation", "pow"));
                nodes.add(token);
            } else if (string.equals("=="))
            {
                token.l("assertion", "equals");//nodes.add(new Token("assertion", "equals"));
                nodes.add(token);
            } else if (string.equals("="))
            {
                token.l("set", "set");//nodes.add(new Token("set", "set"));
                nodes.add(token);
            } else if (string.equals("=>"))
            {
                token.l("set", "copy");//nodes.add(new Token("set", "copy"));
                nodes.add(token);
            } else if (string.equals("->"))
            {
                token.l("dot", "pointer");//nodes.add(new Token("dot", "pointer"));
                nodes.add(token);
            } else if (string.equals("."))
            { //if(nodes.get(nodes.size() - 1).type.equals("input")) {
//                String newvalue = nodes.get(nodes.size() - 1).child.get(0).value + "." + lexer.tokens.get(++ i); nodes.set(nodes.size() - 1, )} else
                token.type = "keyword";//nodes.add(new Token("dot", "heap"));
                nodes.add(token);
            } else if (string.equals(","))
            {
                token.type = "keyword";//nodes.add(new Token("seperator", "seperator"));
            } else if (string.equals("(") & lexer.tokens.size() >= (i + 3) && lexer.tokens.get(i + 1).matches("([A-z]+(\\d?\\_?\\$?)+)") && lexer.tokens.get(i + 2).equals(")") && !nodes.get(nodes.size() - 1).type.equals("identifier"))
            {
//                nodes.add(new Token("cast", lexer.tokens.get(i + 1)));
                nodes.add(token.l("cast", lexer.tokens.get(i + 1)));
//                token.type = "cast";//
                i = i + 2;
                continue;
            } else if (string.equals("("))
            {
                token.l("parenthesis", "open");//nodes.add(new Token("parenthesis", "open"));
                nodes.add(token);
            } else if (string.equals(")"))
            {
                token.l("parenthesis", "closed");//nodes.add(new Token("parenthesis", "closed"));
                nodes.add(token);
            } else if (string.equals("{"))
            {
                token.l("braces", "open");
                nodes.add(token);

//                nodes.add(new Token("braces", "open"));
                for (Token node : nodes) root.add(node);
                nodes.clear();
            } else if (string.equals("}"))
            {
                nodes.add(token.l("braces", "closed"));
            } else if (string.equals(";"))
            {
                Token node = new Token("statement", "");
//                mNode.child.addAll(nodes);
                for (Token node1 : nodes) root.add(node1);
                root.add(node);
                nodes.clear();
            } else if (string.startsWith("#"))
            {
                token.l("preprocessor", token.value.substring(1));
                nodes.add(token);
            }
        }

        if (!nodes.isEmpty()) for (Token node : nodes) root.add(node);

//        recursive_braces(root);
//        fix_open_parenthesis(root);
//        recursive_brackets(root);
//        arrange(root);

        preprocess(root);

        if(!all) { this.root = root; return; }

        check_for_pointers(root);
        check_for_references(root);
        recursive_braces(root);
//        fix_open_braces(root);

//        recursive_statements(root);
//        fix_statements(root);

//        recursive_statement(root);
        fix_open_parenthesis(root);
        recursive_brackets(root);
        keyword_defs(root);
        arrange(root);

        do_math(root);

        add_self_parameter_to_structs(root);

//        for(int i = 0; i < 100; i++) recursive_multiplication(root);

//        prepare_bluprints(root);
        Token rn = new Token();
        rn.add(root);
        object_defs(rn);
        object_dot(rn);
        object_sets(rn);

//        object_definitions(root);
//        dots(root);
        listNodes(root, 0);

        this.root = root;
    }

    public void keyword_defs(Token root) throws ParseException
    {
        int index = 0;

        while (root.child.size() > index)
        {
            Token node = root.child.get(index++);

            keyword_defs(node);
            keyword_definitions(node);
        }
    }


    public void keyword_definitions(Token root) throws ParseException
    {
        if (root.type.equals("operation")) return;
        else if(root.type.equals("method")) return;
        else if(root.type.equals("field")) return;
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();

        ArrayList<Token> keywords = new ArrayList<>();

        while (!stream.pvNext().type.equals("empty"))
        {
            Token node = stream.next();

            if(node.type.equals("keyword"))
            {
                keywords.add(node);
            } else if(!node.type.equals("keyword"))
            {
                if(node.type.equals("class") || node.type.equals("struct"))
                {
                    if(keywords.size() > 0)
                    {
                        System.err.println("err: " + node.type + "s" + " can't be " + keywords + " at line: " + node.line + " : " + node.begin());
                        System.exit(0);
                    }
                }
                for(Token token : keywords) node.setKeyWord(token);
                tokens.add(node);
                keywords.clear();
            } else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    public void preprocess(Token root) throws Exception
    {
        ArrayList<Token> tokens = new ArrayList<>();

        while (root.child.size() > 0)
        {
            if (root.child.get(0).type.equals("preprocessor"))
            {
                if(root.child.get(0).value.equals("include"))
                {
                    String location = root.child.get(1).child.get(0).value;
                    root.child.remove(0);

                    tokens.addAll(new Parser(new Lexer(new Preprocessor(new File(Main.root.toString() + location), null).text()), false).root.child);
                }
                else if (root.child.get(0).value.equals("ifdef") || root.child.get(0).value.equals("ifndef"))
                {
                    if (root.child.get(0).line != root.child.get(1).line)
                    {
                        System.err.println("cj.parse.exception: empty preprocessor '#if' at line " + Lexer.errorByLine(root.child.get(0).line, root.child.get(0).end));
                        System.exit(0);
                    }

                    List<Token> accumulator = new ArrayList<>();
//                    tokens.add(root.child.get(0));
                    Token child = root.child.get(0);
                    child.add(root.child.get(1));
                    root.child.remove(1);
                    Token body  = new Token("container", "#if");
                    child.add(body);
                    body.line= child.line;
                    body.end = child.end;

                    root.child.remove(0);
                    accumulate(root, accumulator);
                    body.child.addAll(accumulator);

                    if(child.value.equals("ifdef"))
                    {
                        if(defines.containsKey(child.child.get(0).value)) tokens.addAll(body.child);
                    } else if(child.value.equals("ifndef"))
                    {
                        if(!defines.containsKey(child.child.get(0).value)) tokens.addAll(body.child);
                    }
                    continue;
                } else if (root.child.get(0).value.equals("define"))
                {
                    Token define = root.child.get(0);
                    Token name = root.child.get(1);
                    defines.put(name.value, "");

                    List<Token> inline = new ArrayList<>();
                    for (int i = 2; i < root.child.size(); i++)
                        if (root.child.get(i).line != define.line) break;
                        else inline.add(root.child.get(i));

                    if (define.line != name.line)
                    {
                        System.err.println("cj.parse.exception: empty preprocessor '#define' at line " + Lexer.errorByLine(define.line, define.end));
                        System.exit(0);
                    }

                    define.add(name);
                    define.child.addAll(inline);
                    root.child.remove(1);
                    root.child.remove(0);
                    for (Token token : inline) root.child.remove(0);

                    tokens.add(define);
                    continue;
                }
            } else tokens.add(root.child.get(0));

            root.child.remove(0);
        }

        root.child.addAll(tokens);
    }

    public void accumulate(Token root, List<Token> accummulator)
    {
        while (root.child.size() > 0)
        {
            if (root.child.get(0).type.equals("preprocessor"))
            {
                if (root.child.get(0).value.equals("ifdef") || root.child.get(0).value.equals("ifndef"))
                {
                    if (root.child.get(0).line != root.child.get(1).line)
                    {
                        System.err.println("cj.parse.exception: empty preprocessor '#if' at line " + Lexer.errorByLine(root.child.get(0).line, root.child.get(0).end));
                        System.exit(0);
                    }

                    List<Token> accumulator2 = new ArrayList<>();
                    Token child = root.child.get(0);
                    child.add(root.child.get(1));
                    root.child.remove(1);
                    Token body  = new Token("container", "#if");
                    child.add(body);
                    body.line= child.line;
                    body.end = child.end;
                    root.child.remove(0);
                    accumulate(root, accumulator2);
                    body.child.addAll(accumulator2);
                    if(child.value.equals("ifdef"))
                    {
                        if(defines.containsKey(child.child.get(0).value)) accummulator.addAll(body.child);
                    } else if(child.value.equals("ifndef"))
                    {
                        if(!defines.containsKey(child.child.get(0).value)) accummulator.addAll(body.child);
                    }
                    continue;
                } else if (root.child.get(0).value.equals("define"))
                {
                    Token define = root.child.get(0);
                    Token name = root.child.get(1);
                    defines.put(name.value, "");

                    List<Token> inline = new ArrayList<>();
                    for (int i = 2; i < root.child.size(); i++)
                        if (root.child.get(i).line != define.line) break;
                        else inline.add(root.child.get(i));

                    if (define.line != name.line)
                    {
                        System.err.println("cj.parse.exception: empty preprocessor '#define' at line " + Lexer.errorByLine(define.line, define.end));
                        System.exit(0);
                    }

                    define.add(name);
                    define.child.addAll(inline);
                    root.child.remove(1);
                    root.child.remove(0);
                    for (Token token : inline) root.child.remove(0);

                    accummulator.add(define);
                    continue;
                }
                else if (root.child.get(0).value.equals("endif"))
                {
                    root.child.remove(0);
                    return;
                }
            }

            accummulator.add(root.child.get(0));
            root.child.remove(0);
        }
    }

    public void object_defs(Token root) throws ParseException
    {
        int index = 0;

        while (root.child.size() > index)
        {
            Token node = root.child.get(index++);

            object_defs(node);
            object_definitions(node);
        }
    }

    public void object_sets(Token root)
    {
        int index = 0;

        while (root.child.size() > index)
        {
            Token node = root.child.get(index++);

            object_sets(node);
            object_sets_(node);
        }
    }

    public void object_dot(Token root)
    {
        int index = 0;

        while (root.child.size() > index)
        {
            Token node = root.child.get(index++);

            object_dot(node);
            object_dot_(node);
        }
    }

    public void object_definitions(Token root) throws ParseException
    {
        if (root.type.equals("operation")) return;
        else if (root.type.equals("field")) return;
        else if (root.type.equals("method") && root.isNativeMethod()) return;
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();
        while (!stream.pvNext().type.equals("empty"))
        {
            Token node = stream.next();

            if ((node.type.equals("identifier") || node.type.equals("pointer") || node.type.equals("reference")) && (stream.pvNext().type.equals("identifier") || stream.pvNext().type.equals("call")))
            {
                Token identifier = stream.next();

                Token field = new Token("field", "field");
                field.add(node);
                field.add(identifier);
//                root.addField(identifier.value, node.value + "*");

                tokens.add(field);
            } else if(node.type.equals("keyword") && node.value.equals("new"))
            {
                Token n = stream.next();
                n.type  = "new";
                tokens.add(n);
            } else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    public void object_sets_(Token root)
    {
        if (root.type.equals("operation")) return;
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();
        while (!stream.pvNext().type.equals("empty"))
        {
            Token node = stream.next();

            if (stream.pvNext().type.equals("set"))
            {
                Token set = stream.next();

                set.add(node);
                set.add(stream.next());

                tokens.add(set);
            } else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    public void object_dot_(Token root)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();
        while (!stream.pvNext().type.equals("empty"))
        {
            Token node = stream.next();

            if (((node.type.equals("identifier")) && (stream.pvNext().type.equals("dot"))))
            {
                Token set = stream.next();

                Token accessor = new Token("access", "access");
                accessor.add(node);
                accessor.add(stream.next());

                while (stream.pvNext().type.equals("dot"))
                {
                    stream.next();
                    accessor.add(stream.next());
                }

                tokens.add(accessor);
            } else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    private void prepare_bluprints(Token root)
    {
        int index = 0;

        while (index < root.child.size())
        {
            Token node = root.child.get(index);

            if (node.type.equals("struct"))
            {
                Token body = node.child.get(0);
                TokenStream stream = body.TokenStream();

                Token next = null;
                while (!(next = stream.next()).type.equals("empty"))
                {

                }
            }

            index++;
        }
    }

//    class StructBlueprint
//    {
//        HashMap<String, Integer> offsets = new HashMap<>();
//        int indexer;
//
//        public void add(String value, Runtime.FieldType type)
//        {
//            offsets.put(value, indexer);
//            indexer = indexer + Runtime.sizeOf(type);
//        }
//
//        public int get(String value)
//        {
//            return offsets.get(value);
//        }
//    }

//    HashMap<String, StructBlueprint> blueprints = new HashMap<String, StructBlueprint>();

    public Token root;

    public static void fix_math(Token root, Token feed, int i)
    {
        int index = i;

        while (index < root.child.size())
        {
            if (root.child.get(index).type.equals("operation"))
            {
//                root.child.get(index).add(root.child.get(index + 1));
//                root.child.get(index).add(root.child.get(index + 2));
                feed.add(root.child.get(index));
                fix_math(root, root.child.get(i), i + 1);
                root.child.remove(index);
            }
            index++;
        }

//        for(Token node : root.child) fix_math(root, root.child.get(i), i + 1);
    }

    public void check_for_pointers(Token root)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();

        while (!stream.pvNext().type.equals("empty"))
        {
            Token prev = stream.last();
            Token node = stream.next();

            if(prev.type.equals("set") && (node.type.equals("operation") && node.value.equals("mul") && stream.pvNext().isIdentifier()))
            {
                tokens.add(new Token("dereference", stream.next().value));
            }
            else if (prev.isCloesed() && (node.type.equals("identifier") && stream.pvNext().type.equals("operation") && stream.pvNext().value.equals("mul"))) // types.get(node.value).equals("struct") &&
            {
                Token ptr = stream.next();
                Token id2 = stream.next();

                if ((id2.type.equals("identifier")) && (stream.pvNext().type.equals("set") || stream.pvNext().type.equals("statement")))
                {
                    tokens.add(new Token("pointer", node.value));
                    tokens.add(id2);
                } else
                {
                    tokens.add(node);
                    tokens.add(id2);
                }
            } else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    public void check_for_references(Token root)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        TokenStream stream = root.TokenStream();

        while (!stream.pvNext().type.equals("empty"))
        {
            Token prev = stream.last();
            Token node = stream.next();
            if(prev.type.equals("set") && (node.type.equals("operation") && node.value.equals("and") && stream.pvNext().isIdentifier()))
            {
                tokens.add(new Token("reference", stream.next().value));
            }
            else if (prev.isCloesed() && (node.type.equals("identifier") && stream.pvNext().type.equals("operation") && stream.pvNext().value.equals("and"))) // types.get(node.value).equals("struct") &&
            {
                Token ptr = stream.next();
                Token id2 = stream.next();

                if ((id2.type.equals("identifier")) && (stream.pvNext().type.equals("set") || stream.pvNext().type.equals("statement")))
                {
                    tokens.add(new Token("reference", node.value));
                    tokens.add(id2);
                } else
                {
                    tokens.add(node);
                    tokens.add(id2);
                }
            }
//            else if (node.type.equals("identifier") && stream.pvNext().type.equals("operation") && stream.pvNext().value.equals("and")) // types.get(node.value).equals("struct") &&
//            {
//                Token ptr = stream.next();
//                Token id2 = stream.next();
//
//                if (id2.type.equals("identifier") && stream.pvNext().type.equals("statement") || stream.pvNext().type.equals("set"))
//                {
//                    tokens.add(new Token("reference", node.value));
//                    tokens.add(id2);
//                } else
//                {
//                    tokens.add(node);
//                    tokens.add(id2);
//                }
//            }
//            else if (stream.pvNext().type.equals("operation") && stream.pvNext().value.equals("and"))// stream.pvNext().type.equals("operation") && stream.pvNext().value.equals("and")) // types.get(node.value).equals("struct") &&
//            {
//                Token ptr = stream.next();
//                Token id2 = stream.next();
//
////                if (id2.type.equals("identifier") && stream.pvNext().type.equals("statement"))
////                {
//                tokens.add(node);
//                    tokens.add(new Token("reference", id2.value));
////                } else
////                {
//////                    tokens.add(node);
//////                    tokens.add(id2);
////                }
//            }
            else tokens.add(node);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
    }

    public void do_math(Token root)
    {
        if(root.type.equals("dereference")) return;
        if(root.type.equals("pointer")) return;
        if(root.type.equals("reference")) return;
        if(root.type.equals("reference_op")) return;
        for (Token node : root.child) do_math(node);

        ArrayList<Token> rootns = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();

        while (!root.child.isEmpty())
        {
            Token node = root.child.get(0);
            if (root.child.get(0).type.equals("operation") || ((root.child.size() >= (2) && root.child.get(1).type.equals("operation"))))
            {
                B:
                while (!root.child.isEmpty())
                {
                    if (root.child.get(0).type.equals("operation") || ((root.child.size() >= (2) && root.child.get(1).type.equals("operation"))))
                        tokens.add(root.child.get(0));
                    else
                    {
                        Token r = new Token("math", "math");
                        r.child.addAll(tokens);
                        r.child.add(root.child.get(0));
                        tokens.clear();

                        do_m(r);
                        rootns.add(r);
//                        listNodes(r, 0);
                        break B;
                    }

                    root.child.remove(0);
                }
            } else rootns.add(node);

            if (root.child.isEmpty()) break;
            root.child.remove(0);
        }

        for (Token node : rootns) root.add(node);
    }

    public void do_m(Token root)
    {
        Stack<Token> out = new Stack<>();
        Stack<Token> opr = new Stack<>();

        for (Token node : root.child)
        {
            math(node, out, opr);
        }

        while (!opr.isEmpty())
        {
            out.push(opr.pop());
        }

        root.child.clear();

        Stack<Token> r = new Stack<>();
        Stack<Token> l = new Stack<>();

        while (!out.isEmpty())
        {
            if (out.peek().type.equals("operation")) r.push(out.pop());
            else r.peek().add(out.pop());
        }

//        while(!r.isEmpty()) l.push(r.pop());
        while (!r.isEmpty()) root.add(r.pop());

//        Token node = new Token("r", "r");
//        fix_math(root, node, 0);

//        root.child.clear();
//        root.child.addAll(node.child);
    }

    static final HashMap<String, Integer> __OPERATORPRECEDENCE__ = new HashMap<>();
    static final HashMap<String, Integer> __ASSOSCIATIVEPRECEDENCE__ = new HashMap<>();

    static
    {
        __OPERATORPRECEDENCE__.put("mul", 3);
        __OPERATORPRECEDENCE__.put("div", 3);
        __OPERATORPRECEDENCE__.put("add", 2);
        __OPERATORPRECEDENCE__.put("sub", 2);
        __OPERATORPRECEDENCE__.put("pow", 4);
        __OPERATORPRECEDENCE__.put("and", 5);
        __OPERATORPRECEDENCE__.put("or", 5);

        __ASSOSCIATIVEPRECEDENCE__.put("mul", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("and", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("or", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("div", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("add", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("sub", -1);
        __ASSOSCIATIVEPRECEDENCE__.put("pow", 1);
    }

    public static void math(Token root, Stack<Token> out, Stack<Token> op)
    {
        if (root.type.equals("operation"))
        {
            while (!op.isEmpty() && ((__ASSOSCIATIVEPRECEDENCE__.get(root.value) == -1 && (__OPERATORPRECEDENCE__.get(op.peek().value) >= __OPERATORPRECEDENCE__.get(root.value))) || (__ASSOSCIATIVEPRECEDENCE__.get(root.value) == 1 && __OPERATORPRECEDENCE__.get(root.value) < __OPERATORPRECEDENCE__.get(op.peek().value))))
                out.push(op.pop());
            op.push(root);
        } else if (root.type.equals("identifier") || root.type.equals("input")) out.push(root);
        else return;
    }

    public static Token getMathOperation(ArrayList<Token> tree, int index)
    {
        ArrayList<Token> tokens = new ArrayList<>();
//        while(tree.size() > 0)
        {
            Token left = tree.get(index);
            if (tree.size() > (index + 1) && tree.get(1).type.equals("operation"))
            {
                Token operation = tree.get(1);
                if (tree.size() > 2 && tree.get(3).type.equals("operation"))
                {
                    tree.remove(index); //left
                    tree.remove(index); //operation
                    Token right = getMathOperation(tree, index);

                    if (operation.value.equals("add") | operation.value.equals("sub"))
                    {
                        operation.add(left);
                        operation.add(right);
                    } else
                    {
                        if (right.value.equals("add") | operation.value.equals("sub"))
                        {
                            operation.child.addAll(right.child);
                            right.child.clear();


                        }
                    }
                } else if (operation.value.equals("add") | operation.value.equals("sub"))
                {

                } else
                {

                }
            } else tokens.add(left);
//            tree.remove(0);
        }

        return null;
    }

    public static Token getMathOperation2(ArrayList<Token> tree, int index)
    {
        Token node = tree.get(index);

        Token left = tree.get(index - 1);
        Token right = null;

        if (tree.size() > (index + 2) && tree.get(index + 2).type.equals("operation"))
        {
            while (tree.size() > (index + 2) && tree.get(index + 2).type.equals("operation"))
            {
                if (tree.get(index + 2).value.equals("mul") | tree.get(index + 2).value.equals("div") | tree.get(index + 2).value.equals("pow") | tree.get(index + 2).value.equals("mod"))
                {
                    right = getMathOperation2(tree, index + 2);
                    tree.remove(index - 1);
                    tree.remove(index - 1);
                    node.add(left);
                    node.add(right);
                } else if (tree.get(index + 2).value.equals("left") | tree.get(index + 2).value.equals("right"))
                {
                    right = getMathOperation2(tree, index + 2);
                    tree.remove(index - 1);
                    tree.remove(index - 1);
                    node.add(left);
                    node.add(right);
                } else
                {
                    right = tree.get(index + 1);
                    node.add(left);
                    node.add(right);
                    tree.remove(index - 1);
                    tree.remove(index - 1);
                    tree.set(index - 1, node);
//                    node = java.engine.math;
                    if (tree.size() > (index + 2) && tree.get(index + 2).type.startsWith("operation"))
                        node = getMathOperation2(tree, index);
                    else
                    {
                        left = node;
                        right = tree.get(index + 1);
                        node = tree.get(index);
                        node.add(left);
                        node.add(right);
                        tree.remove(index - 1);
                        tree.remove(index - 1);
                        tree.remove(index - 1);
                    }
                }
            }
        } else
        {
            right = tree.get(index + 1);
            tree.remove(index - 1);
            tree.remove(index - 1);
            tree.remove(index - 1);
            node.add(left);
            node.add(right);
        }

        return node;
    }

    private static void recursive_multiplication(Token root)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        for (int i = 0; i < root.child.size(); i++)
        {
            Token node = root.child.get(i);

            if (node.type.equals("operation"))
            {
                if (i > 0)
                {
                    Token last = tokens.get(tokens.size() - 1);
                    tokens.set(tokens.size() - 1, node);
                    node.add(last);
                }
//                i > 0 && root.child.size() > (i + 2) &&

            }
        }
//
//        while (!root.child.isEmpty())
//        {
//            int size = root.child.size() - 1;
//            Token node = root.child.get(size);
//
//            if(root.child.size() >= 3 && root.child.get(size - 1).type.equals("operation") & root.child.get(size - 1).value.equals("mul"))
//            {
//                Token test = root.child.get(size - 1);
//                test.add(root.child.get(size - 2));
//                test.add(node);
//
//                tokens.add(test);
//
//                root.child.remove(root.child.size() - 1);
//                root.child.remove(root.child.size() - 1);
//
        if (root.child.size() >= 3 && root.child.get(root.child.size() - 2).type.equals("operation") && root.child.get(root.child.size() - 2).value.equals("mul"))
        {
            recursive_multiplication(root);
        }
//            } else tokens.add(node);
//            root.child.remove(root.child.size() - 1);
//        }

//        for(Token node : tokens) root.add(node);

        for (Token node : root.child) recursive_multiplication(node);
    }

    private static void recursive_statement(Token root)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("statement"))
            {
                node.child.addAll(tokens);
                tokens.clear();
            } else tokens.add(node);

            root.child.remove(0);
        }

        for (Token node : tokens) root.add(node);

        for (Token node : root.child) recursive_statement(node);
    }

    private static void recursive_brackets(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("bracket") & node.value.equals("open"))
            {
                node.value = "brackets";
                root.child.remove(0);
                get_in_brackets(root, node);

                nodes.add(node);
                continue;
            } else nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) recursive_brackets(node);

        for (Token node : nodes) root.add(node);
    }

    private static void get_in_brackets(Token root, Token out)
    {
        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("bracket") & node.value.equals("open"))
            {
                node.value = "brackets";
                root.child.remove(0);
                get_in_brackets(root, node);
                out.add(node);
                continue;
            } else if (node.type.equals("bracket") & node.value.equals("closed"))
            {
                root.child.remove(0);
                return;
            } else out.add(node);

            root.child.remove(0);
        }
    }

    private void dots(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("identifier") && root.child.size() > 1 && root.child.get(1).type.equals("dot"))
            {
                //ACCESSOR
                Token dot = root.child.get(1);

                if (dot.value.equals("heap"))
                {
                    Token after = root.child.get(2);

                    if (after.type.equals("call"))
                    {
                        ArrayList<Token> nodes1 = new ArrayList<>();
                        nodes1.add(new Token("identifier", node.value));
                        nodes1.add(new Token("seperator", "seperator"));
                        nodes1.addAll(after.child.get(0).child);
                        after.child.get(0).child.clear();
                        after.child.get(0).child.addAll(nodes1);
                    }
                } else
                {

                }
            }

            root.child.remove(0);
        }
    }

    private void arrange(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (root.child.size() >= 2 && root.child.get(0).type.equals("identifier") && (root.child.get(1).type.equals("cast") || root.child.get(1).type.equals("parenthesis")))
            {
                node.type = "call";
                node.add(root.child.get(1));
                if (node.child.get(0).type.equals("cast"))
                    node.child.get(0).add(new Token("identifier", node.child.get(0).value));
                node.child.get(0).type = "parenthesis";
                node.child.get(0).value = "parenthesis";

                nodes.add(node);

                root.child.remove(0);
            } else if(root.child.size() >= 5 && root.child.get(0).value.equals("native"))
            {
                Token method = new Token("method", root.child.get(2).value);
                method.method = true;
                method.nativeMethod = true;
                method.add(root.child.get(3));
                method.add(root.child.get(1));

                root.child.remove(1);
                root.child.remove(1);
                root.child.remove(1);
                nodes.add(method);
            }
            else if (root.child.size() >= 4 && root.child.get(3).type.equals("body") && root.child.get(1).type.equals("identifier"))
            {
                Token method = new Token("method", root.child.get(1).value);
                method.method = true;
                method.add(root.child.get(3));
                method.add(root.child.get(2));
                method.add(root.child.get(0));

                root.child.remove(0);
                root.child.remove(0);
                root.child.remove(0);
                nodes.add(method);
            } else if (root.child.size() >= 3 && node.type.equals("keyword") && node.value.equals("struct"))
            {
                Token struct = new Token("struct", root.child.get(1).value);
                struct.end = root.child.get(1).end;
                struct.line = root.child.get(1).line;
                struct.begin = root.child.get(1).begin;
                types.put(struct.value, struct.type);

                struct.add(root.child.get(2));

                root.child.remove(0);
                root.child.remove(0);

                nodes.add(struct);
            } else if (root.child.size() >= 3 && node.type.equals("keyword") && node.value.equals("class"))
            {
                Token struct = new Token("class", root.child.get(1).value);
                struct.end = root.child.get(1).end;
                struct.line = root.child.get(1).line;
                struct.begin = root.child.get(1).begin;
                types.put(struct.value, struct.type);

                struct.add(root.child.get(2));

                root.child.remove(0);
                root.child.remove(0);

                nodes.add(struct);
            } else nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) root.add(node);

        for (Token node : root.child) arrange(node);
    }

    private void add_self_parameter_to_structs(Token root)
    {
        int index = 0;
        while (index < root.child.size())
        {
            Token node = root.child.get(index++);
            if (node.type.equals("struct")) add_self_parameter(node.child.get(0), node);
        }

//        for(Token node : root.child) add_self_parameter_to_structs(node);
    }

    private void add_self_parameter(Token root, Token struct)
    {
        ArrayList<Token> tokens = new ArrayList<>();

        while (root.child.size() > 0)
        {
            if (root.child.get(0).type.equals("method"))
            {
                ArrayList<Token> nodes1 = new ArrayList<>();

                Token parenthesis = root.child.get(0).child.get(1);

                nodes1.add(new Token("identifier", struct.value));
                nodes1.add(new Token("identifier", "self"));
                nodes1.add(new Token("seperator", "seperator"));

                nodes1.addAll(parenthesis.child);
                parenthesis.child.clear();

                parenthesis.child.addAll(nodes1);
            }

            tokens.add(root.child.get(0));
            root.child.remove(0);
        }

        root.child.clear();
        for (Token node : tokens) root.add(node);
//        for(Token node : root.child) add_self_parameter(node, struct);
    }

    private static void fix_statements(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("body"))
            {
                fix_statements(node);
                nodes.add(node);
                root.child.remove(0);

                continue;
            }

            fix_statements(node, nodes);
            nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) root.add(node);
    }

    private static void fix_open_parenthesis(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("parenthesis") & node.value.equals("open"))
            {
                node.value = "parenthesis";
                root.child.remove(0);
                get_in_parenthesis(root, node);

                nodes.add(node);
                continue;
            } else nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) fix_open_parenthesis(node);

        for (Token node : nodes) root.add(node);
    }

    private static void get_in_parenthesis(Token root, Token out)
    {
        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("parenthesis") & node.value.equals("open"))
            {
                node.value = "parenthesis";
                root.child.remove(0);
                get_in_parenthesis(root, node);
                out.add(node);
                continue;
            } else if (node.type.equals("parenthesis") & node.value.equals("closed"))
            {
                root.child.remove(0);
                return;
            } else out.add(node);

            root.child.remove(0);
        }
    }

    private static void fix_open_braces(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("bracket") & node.value.equals("open"))
            {
//                mNode.value = "body";
                node.type = "body";
                root.child.remove(0);
                get_in_braces(root, node);

                nodes.add(node);
                continue;
            } else nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) fix_open_braces(node);

        for (Token node : nodes) root.add(node);
    }

    private static void get_in_braces(Token root, Token out)
    {
        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);

            if (node.type.equals("bracket") & node.value.equals("open"))
            {
                node.value = "body";
                root.child.remove(0);
                get_in_parenthesis(root, node);
                out.add(node);
                continue;
            } else if (node.type.equals("bracket") & node.value.equals("closed"))
            {
                root.child.remove(0);
                return;
            } else out.add(node);

            root.child.remove(0);
        }
    }

    private static void fix_statements(Token root, ArrayList<Token> nodes)
    {
        ArrayList<Token> nodes0 = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);
            if (node.type.equals("statement"))
            {
                fix_statements(node, nodes);
                nodes.add(node);
            } else nodes0.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes0) root.add(node);
    }

    private static void recursive_statements(Token root)
    {
        for (int i = 0; i < root.child.size(); i++)
        {
            if (root.child.get(i).type.equals("statement"))
            {
                recursive_statments(root, root.child.get(i), i + 1);
                recursive_statements(root.child.get(i));
            } else recursive_statements(root.child.get(i));
        }
    }

    private static void recursive_statments(Token root, Token statement, int index)
    {
        while (root.child.size() > index)
        {
            if (root.child.get(index).type.equals("statement")) return;
            else
            {
                statement.add(root.child.get(index));
                root.child.remove(index);
            }
        }
    }

    private static void recursive_braces(Token root)
    {
        ArrayList<Token> nodes = new ArrayList<>();

        while (root.child.size() > 0)
        {
            Token node = root.child.get(0);
//            if(root.child.get(0).type.equals("statement")) { if(st != null) { st.child.addAll(addTo); recursive_braces(st); nodes.add(st); } addTo = new ArrayList<>(); st = mNode; }
            if (root.child.get(0).type.equals("braces") && node.value.equals("open"))
            {
                addTillClosed(root, 0);
                nodes.add(node);
            } else nodes.add(node);

            root.child.remove(0);
        }

        for (Token node : nodes) recursive_braces(node);
        for (Token node : nodes) root.add(node);
    }

    private static void addTillClosed(Token root, int i)
    {
        while (root.child.size() > 1 + i)
        {
            Token node = root.child.get(1 + i);
            if (node.type.equals("braces") && node.value.equals("open")) addTillClosed(root, i + 1);
            else if (node.type.equals("braces") && node.value.equals("closed"))
            {
                root.child.remove(1 + i);
                root.child.get(i).type = "body";
                return;
            } else
            {
                root.child.get(i).add(node);
                root.child.remove(1 + i);
            }
        }
    }

    public static String tab(int size)
    {
        String string = "";

        for (int i = 0; i < size; i++) string += "    ";

        return string;
    }

    public static void listNodes(Token node, int tab)
    {
        for (Token n : node.child)
        {
            System.out.println(tab(tab) + n.type + " " + n.value);
            listNodes(n, tab + 1);
        }
    }
}