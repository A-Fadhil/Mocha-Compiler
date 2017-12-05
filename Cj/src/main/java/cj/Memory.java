package cj;

import cj.MemoryBlock;

import java.util.HashSet;

public class Memory
{
//    Stack<MemBlock> memBlockStack;
//
//    class MemBlock{
//        byte block[] = new byte[256];
//        int  reservd = 0;
//
//        boolean available(int size)
//        {
//            if(reservd + size >= 256)
//        }
//
//        byte res(int size)
//        {
//        }
//    }

//    private MemoryBlock memory[];
    HashSet<MemoryBlock> memory;
//    private int  inform[];

    int siz;
    int max;

    public Memory()
    {
        memory = new HashSet<>();
    }

    public MemoryBlock expand(int needed)
    {
        if (needed + siz >= max)
        {
            System.err.println("out of memory.");
            System.err.println("needed: " + needed);
            System.err.println("size:   " + siz);
            System.err.println("max:    " + max);
            System.exit(0);
        }

        siz = siz + needed;

        MemoryBlock block = new MemoryBlock(needed);
        memory.add(block);

        return block;
    }

    public static int twiddle(int n)
    {
        n--;
        n |= n >> 1;   // Divide by 2^k for consecutive doublings of k up to 32,
        n |= n >> 2;   // and then or the results.
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;

        return n;
    }

    public int malloc(int size)
    {
        int malloc = twiddle(size);

        System.out.println(malloc);
        return 0;
    }
}