package cj.helpers;

public class DynamicStack
{
    int stack[];
    int index;

    public DynamicStack()
    {
        this(10);
    }

    public DynamicStack(int initialStackSize)
    {
        stack = new int[initialStackSize];
    }

    public void push(int i)
    {
        if(index + 1 >= stack.length)
        {
            int nstack[] = new int[(int) (stack.length * 1.1)];
            System.arraycopy(stack, 0, nstack, 0, stack.length);

            stack = nstack;
        } else if(index < stack.length / 1.1)
        {
            int nstack[] = new int[(int) (stack.length / 1.1)];
            System.arraycopy(stack, 0, nstack, 0, nstack.length);

            stack = nstack;
        }
        stack[index ++] = i;
    }

    public int pop()
    {
        return stack[-- index];
    }
    public int peek() { return stack[index - 1]; }
}