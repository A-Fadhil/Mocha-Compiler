package cj;

public class Method extends Container
{
    public static int methods = 0;

    Token method;
    boolean inline;
    int id;

    private boolean constructor;

    public Method(Token token)
    {
        method = token;
        id = methods++;

        opCode = new OpCode(token.stubValue());
        genOpCode();
        method.m = this;
    }

    public void genOpCode()
    {
        AbstractSyntaxTree.tree.createOpCode(true, opCode, method);
        System.out.println(opCode.humanReadable());
        System.out.println(opCode.mOpCode + " " + opCode.mOpCode.size());
    }

    public void call(OpCode opCode, boolean canInline)
    {
        if(inline & canInline)
            opCode.append(method.opcode);
        else {
            opCode.call(id);
        }
    }

    public boolean matchParameters(Token token)
    {
        int size = 0;
        for(Token token1 : method.child.get(1)) if(!token1.type.equals("seperator")) size = size + 1;
        int size2 = 0;
        for(Token token1 : token.child) if(!token1.type.equals("seperator")) size2 = size2 + 1;

        return (size2 == (size));
    }

    public boolean isConstructor()
    {
        return constructor;
    }

    public void setConstructor(boolean constructor)
    {
        this.constructor = constructor;
    }
}