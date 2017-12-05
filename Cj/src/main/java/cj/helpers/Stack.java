package cj.helpers;

public class Stack
{
    private long pointer[];
    private byte byttype[];
    private int  index;

    public static final byte
            BYTE = 0,
            SHORT = 1,
            CHAR = 2,
            INTEGER = 3,
            FLOAT = 4,
            LONG = 5,
            DOUBLE = 6,
            REFERENCE = 7,
            POINTER = 8,
            BYTEc = 9,
            SHORTc = 10,
            CHARc = 11,
            INTEGERc = 12,
            FLOATc = 13,
            LONGc = 14,
            DOUBLEc = 15;

    public Stack(int size)
    {
        pointer = new long[size];
        byttype = new byte[size];
    }

    public void push(String string)
    {
        index ++;
    }

    public void push(long pointer)
    {
        this.pointer[index ++] = pointer;
    }

    public void iload(int val)
    {
        this.pointer[index ++] = val;
        this.byttype[index - 1]= INTEGER;
    }

    public void lload(long val)
    {
        this.pointer[index ++] = val;
        this.byttype[index - 1]= LONG;
    }

    public void push(long pointer, byte type)
    {
        this.pointer[index ++] = pointer;
        this.byttype[index - 1] = type;
    }

    public long pop() { return pointer[-- index]; }

    public int popInt()
    {
        return 9;
    }
}