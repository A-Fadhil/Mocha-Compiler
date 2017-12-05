package cj.util;

import cj.Runtime;
import cj.helpers.DynamicStack;
import cj.helpers.DynamicStackByte;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by Aboody on 04/10/2017.
 */
public class Stack
{
    private byte MEMORY[];
    private int  index;
    private DynamicStack stack;
    private DynamicStackByte tstack;

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
            DOUBLEc = 15,
            STRING = 16;

    public Stack(int size)
    {
        MEMORY = new byte[size];
        stack  = new DynamicStack(20);
        tstack = new DynamicStackByte(20);
    }

    public void push(ByteBuffer struct)
    {
        int start = index;
        push(struct.capacity());

        for(int i = start; i < index; i++) MEMORY[i] = struct.get();
        struct.rewind();
    }

    public void push(Object object)
    {
        Class clss = object.getClass();
        int size = 0;
        for (Field f : clss.getDeclaredFields())
        {
//            if(f.getType().toString().equals("int")) size = size + 4;
//            else if(f.getType().toString().equals("float")) size = size + 4;
//            else if(f.getType().toString().equals("long")) size = size + 8;
//            else if(f.getType().toString().equals("double")) size = size + 8;
//            else if(f.getType().toString().equals("char")) size = size + 2;
//            else if(f.getType().toString().equals("short")) size = size + 2;
//            else if(f.getType().toString().equals("byte")) size = size + 1;
            if(f.getType().toString().equals("int")) size = size + 8;
            else if(f.getType().toString().equals("float")) size = size + 8;
            else if(f.getType().toString().equals("long")) size = size + 8;
            else if(f.getType().toString().equals("double")) size = size + 8;
            else if(f.getType().toString().equals("char")) size = size + 8;
            else if(f.getType().toString().equals("short")) size = size + 8;
            else if(f.getType().toString().equals("byte")) size = size + 8;
        }
        ByteBuffer struct = ByteBuffer.allocateDirect(size);
        size = 0;
        Object array[] = {object};
        long baseOffset   = Runtime.unsafe.arrayBaseOffset(Object[].class);
        long objectAddress= Runtime.unsafe.getLong(array, baseOffset);

//        System.out.println(Runtime.unsafe.getByte(objectAddress));

        for (Field f : clss.getDeclaredFields())
        {
//            if(f.getType().toString().equals("int")) size = size + 4;
//            else if(f.getType().toString().equals("float")) size = size + 4;
//            else if(f.getType().toString().equals("long")) size = size + 8;
//            else if(f.getType().toString().equals("double")) size = size + 8;
//            else if(f.getType().toString().equals("char")) size = size + 2;
//            else if(f.getType().toString().equals("short")) size = size + 2;
//            else if(f.getType().toString().equals("byte")) size = size + 1;
            if(f.getType().toString().equals("int"))
            {

                size = size + 8;
            }
            else if(f.getType().toString().equals("float")) size = size + 8;
            else if(f.getType().toString().equals("long")) size = size + 8;
            else if(f.getType().toString().equals("double")) size = size + 8;
            else if(f.getType().toString().equals("char")) size = size + 8;
            else if(f.getType().toString().equals("short")) size = size + 8;
            else if(f.getType().toString().equals("byte")) size = size + 8;
        }

        tstack.push(REFERENCE);
        push(size);
    }

    private void push(int blocksize)
    {
        if(index + blocksize >= MEMORY.length)
        {
            System.err.println("cj.stack: StackOverflowError;");
            System.exit(0);
        }

        stack.push(index);
        index = index + blocksize;
    }

    private int pop()
    {
        tstack.pop();
        index = stack.pop();
        return index;
    }

    public void pushInt(int value)
    {
        tstack.push(INTEGER);
        int lc = index;
        push(4);

        MEMORY[lc] = (byte) (value>>24);
        MEMORY[lc + 1] = (byte) (value>>16);
        MEMORY[lc + 2] = (byte) (value>>8);
        MEMORY[lc + 3] = (byte) (value);
    }

    public int popInt()
    {
        int lc = pop();
        return MEMORY[lc]<<24 | MEMORY[lc + 1]<<16 | MEMORY[lc + 2]<<8 | MEMORY[lc + 3];
    }

    public void pushLong(long value)
    {
        tstack.push(LONG);
        int lc = index;
        push(8);

        MEMORY[lc + 0] = (byte) (value>>56);
        MEMORY[lc + 1] = (byte) (value>>48);
        MEMORY[lc + 2] = (byte) (value>>40);
        MEMORY[lc + 3] = (byte) (value>>32);
        MEMORY[lc + 4] = (byte) (value>>24);
        MEMORY[lc + 5] = (byte) (value>>16);
        MEMORY[lc + 6] = (byte) (value>>8);
        MEMORY[lc + 7] = (byte) (value);
    }

    public long popLong()
    {
        int lc = pop();
        return MEMORY[lc]<<56 | MEMORY[lc + 1]<<48 | MEMORY[lc + 2]<<40 | MEMORY[lc + 3]<<32 | MEMORY[lc + 4]<<24 | MEMORY[lc + 5]<<16 | MEMORY[lc + 6]<<8 | MEMORY[lc + 7];
    }

    public void pushFloat(float value)
    {
        tstack.push(FLOAT);
        pushInt(Float.floatToRawIntBits(value));
    }

    public void pushDouble(double value)
    {
        tstack.push(DOUBLE);
        pushLong(Double.doubleToRawLongBits(value));
    }

    public float popFloat()
    {
        return Float.intBitsToFloat(popInt());
    }

    public double popDouble()
    {
        return Double.longBitsToDouble(popLong());
    }

    public void pushString(String string)
    {
        tstack.push(STRING);
        int lc = index;
        push(string.length() * 2);

        int mi = lc;

        for(int i = 0; i < string.length(); i ++)
        {
            MEMORY[mi ++] = (byte) (string.charAt(i) >> 8);
            MEMORY[mi ++] = (byte) (string.charAt(i));
        }
    }

    public String popString()
    {
        int to = index;
        int lc = pop();

        String string = "";

        for(int i = lc; i < to; i += 2)
        {
            string = string +
                    (char) ((MEMORY[i] << 8) |
             (MEMORY[i + 1]));
        }

        return string;
    }

    public void pushChar(char value)
    {
        tstack.push(CHAR);
        int lc = index;
        push(2);
        MEMORY[lc ++] = (byte) (value >> 8);
        MEMORY[lc ++] = (byte) (value);
    }

    public void pushShort(short value)
    {
        tstack.push(SHORT);
        int lc = index;
        push(2);
        MEMORY[lc ++] = (byte) (value >> 8);
        MEMORY[lc ++] = (byte) (value);
    }

    public char popChar()
    {
        int i = pop();

        return (char) ((MEMORY[i] << 8) |
                (MEMORY[i + 1]));
    }

    public short popShort()
    {
        int i = pop();

        return (short) ((MEMORY[i] << 8) |
                (MEMORY[i + 1]));
    }

    public void print()
    {
        switch (tstack.peek())
        {
            case REFERENCE: System.out.println("0&x" + Integer.toHexString(pop())); break;
            case STRING: System.out.println(popString()); break;
            case POINTER: System.out.println("0x" + Integer.toHexString(popInt())); break;
            case FLOAT: System.out.println(popFloat()); break;
            case DOUBLE: System.out.println(popDouble()); break;
            case CHAR: System.out.println(popChar()); break;
            case SHORT: System.out.println(popShort()); break;
            case INTEGER: System.out.println(popInt()); break;
            case LONG: System.out.println(popLong()); break;
            case FLOATc: System.out.println("f0&x" + Integer.toHexString(popInt())); break;
            case DOUBLEc: System.out.println("d0&x" + Integer.toHexString(popInt())); break;
            case CHARc: System.out.println("c0&x" + Integer.toHexString(popInt())); break;
            case SHORTc: System.out.println("s0&x" + Integer.toHexString(popInt())); break;
            case INTEGERc: System.out.println("i0&x" + Integer.toHexString(popInt())); break;
            case LONGc: System.out.println("l0&x" + Integer.toHexString(popInt())); break;
        }
    }
}