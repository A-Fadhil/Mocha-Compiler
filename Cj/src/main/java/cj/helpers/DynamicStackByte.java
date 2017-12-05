package cj.helpers;

public class DynamicStackByte
{
    byte stack[];
    int index;

    public DynamicStackByte()
    {
        this(10);
    }

    public DynamicStackByte(int initialStackSize)
    {
        stack = new byte[initialStackSize];
    }

    public void push(byte b)
    {
        if(index + 1 >= stack.length)
        {
            byte nstack[] = new byte[(int) (stack.length * 1.1)];
            System.arraycopy(stack, 0, nstack, 0, stack.length);

            stack = nstack;
        } else if(index < stack.length / 1.1)
        {
            byte nstack[] = new byte[(int) (stack.length / 1.1)];
            System.arraycopy(stack, 0, nstack, 0, nstack.length);

            stack = nstack;
        }
        stack[index ++] = b;
    }

    public byte pop()
    {
        return stack[-- index];
    }
    public byte peek() { return stack[index - 1]; }
}