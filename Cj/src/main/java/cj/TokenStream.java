package cj;

import java.util.ArrayList;

public class TokenStream
{
    ArrayList<Token> nodes = new ArrayList<>();
    int index = 0;
    Token NULL = Parser.NULL;
    boolean mNull;
    public Token force;

    public TokenStream(Token node) { nodes.addAll(node.child); force = node; }

    public int remaining() { return nodes.size() - index; }
    public int capacity()  { return nodes.size(); }
    public int position()  { return index; }
    public Token next() { if(nodes.size() > index) return nodes.get(index ++); else return NULL; }
    public Token last() { if(index > 0) return nodes.get(index - 1); else return NULL; }
    public void back()        { index --; }
    public void rewind()      { index = 0; }

    public Token pvNext() { if(nodes.size() > index) return nodes.get(index); else return mNull ? null : NULL; }

    public TokenStream nullable()
    {
        mNull = true;
        return this;
    }
}