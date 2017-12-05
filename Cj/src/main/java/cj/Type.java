package cj;

import java.util.ArrayList;
import java.util.HashMap;

public class Type extends Container
{
    public static HashMap<String, Type> registered_types = new HashMap<>();
    public Token token;
    ArrayList<Method> methodSet = new ArrayList<>();
    Method initializer;

    public Type(Token token)
    {
        this.token = token;

        Token body = token.child.get(0);
        for (Token field : body)
        {
            if(field.isField()) fieldSet.add(new Field(field));
        }

        TokenStream stream = token.child.get(0).TokenStream();
        stream.nullable();

        Token init = new Token();
        init.line = token.line;
        init.begin= token.begin;
        init.parent = token;
        init.value  = token.value;

        while(stream.pvNext() != null)
        {
            Token next = stream.next();
            if(next.isMethod() && !next.isNativeMethod())
            {
                Method method = new Method(next);
                if (next.value.equals(token.value)) method.setConstructor(true);
                methodSet.add(method);
            } else if(next.type.equals("set"))
            {
            }
        }

        initializer = new Method(init);
    }

    public boolean isChildOf(Type other)
    {
        if(fieldSet.size() < other.fieldSet.size()) return false;
        for(int i = 0; i < other.fieldSet.size(); i ++) if(!(other.fieldSet.get(i).typeName.equals(fieldSet.get(i).typeName) && other.fieldSet.get(i).name.equals(fieldSet.get(i).name))) return false;

        return true;
    }

    public Method getConstructor(Token token)
    {
        for(Method method : methodSet)
            if(method.isConstructor() && method.matchParameters(token.child.get(0))) return method;

//        if(token.child.get(0).child.size() == 0) return initializer;

        System.err.println("no constructor for '" + this.token.value + "' matching parameters: " + token.child.get(0).child + " at line: " + token.line + " : " + token.begin());
        System.exit(0);

        return initializer;
    }
}