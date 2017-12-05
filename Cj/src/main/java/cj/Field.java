package cj;

public class Field
{
    Type type;
    String    typeName;
    String    name;
    Token     token;

    public Field(Token field)
    {
        token = field;
        name  = field.child.get(1).value;
        typeName = field.child.get(0).type;
    }

    public void passByReference(OpCode opCode)
    {
    }

    public void passByValue(OpCode opCode)
    {
    }
}