package cj;

import cj.helpers.BinarySearchTree;
import cj.util.Stack;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;

public class Runtime
{
    private static Runtime runtime;
    public static Unsafe   unsafe;
    private byte MEMORY[];
//    private List<Long> fragments;
    private Stack STACK;
    private HashSet<Long> FREE_MEMORY_FRAGMENTS;
    private HashSet<Long> USED_MEMORY_FRAGMENTS;
    private HashSet<Long> REMV_MEMORY_FRAGMENTS;

    public Runtime(int stacksize, int memsize, boolean gc)
    {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception x) {
            x.printStackTrace();
        }

        memsize = Memory.twiddle(memsize);
        stacksize = Memory.twiddle(stacksize);

        runtime = this;
        MEMORY = new byte[memsize];
//        fragments = new ArrayList<>();
        long l = fragment(0, MEMORY.length);
        FREE_MEMORY_FRAGMENTS = new HashSet<>();
        USED_MEMORY_FRAGMENTS = new HashSet<>();
        REMV_MEMORY_FRAGMENTS = new HashSet<>();
        FREE_MEMORY_FRAGMENTS.add(l);
//        fragments.add(l);
        STACK = new Stack(stacksize);

        STACK.pushLong(555);
        STACK.pushLong(556);
        STACK.pushString("hello world");
        STACK.pushChar('a');
        STACK.push(new Vector3(15));

//        System.out.println(STACK.popChar() + " " + STACK.popString() + " " + STACK.popLong() + " " + STACK.popLong());

        STACK.print();
        STACK.print();

//        System.out.println(malloc(5));
//        System.out.println(malloc(5));
//        System.out.println(malloc(5));

//        memUtilPrintMemoryUsage();
//
//        free(0);
//        free(5);
//
//        memUtilPrintMemoryUsage();
    }

    public static final int
            malloc = 0,
            calloc = 1,
            realloc = 2,
            delete = 3,
            push_i = 4,
            push_f = 5,
            push_l = 6,
            push_d = 7,
            push_c = 8,
            push_s = 9,
            push_a = 10,
            push_r = 11,

            multiply = 12,
            add = 13,
            subtract = 14,
            divide = 15,
            raise = 16,
            sin = 17,

            call= 18,
            trye= 19,
            ctch= 20,
            thrw= 21,
            load= 22,
            print=23,
            corce=24,

            halt = 255;

    public static BinarySearchTree<ByteBuffer> buffers = new BinarySearchTree<ByteBuffer>(16);

    private void run(ByteBuffer code)
    {
        int arguments = Byte.toUnsignedInt(code.get());
        long args[]   = new long[arguments];
//        for(int i = arguments - 1; i > - 1; i --) args[i] = STACK.pop();

        while(code.remaining() > 0)
        {
            switch (code.get())
            {
                case call: run(buffers.get(STACK.popInt())); break;

                case malloc: STACK.pushInt(malloc(STACK.popInt())); break;
                case calloc: STACK.pushInt(calloc(STACK.popInt())); break;
                case realloc:
                    int ptr = STACK.popInt();
                    int sze = STACK.popInt();

                    long l = getUsed(ptr);
                    int size= fragment_size(l);
                    free(ptr);

                    l = malloc(sze);
                    for(int i = 0; i < size; i++) MEMORY[(int) (l + i)] = MEMORY[ptr + i];

                    STACK.pushInt((int) l);
                    break;
            }
        }
    }

    private void memUtilPrintMemoryUsage()
    {
        System.out.println("------------------USED-------------------");
        for(Long l : USED_MEMORY_FRAGMENTS)
        {
            System.out.println("[" + fragment_size(l) + ":" + fragment_pos(l) + "]");
        }
        System.out.println("------------------FREE-------------------");
        for(Long l : FREE_MEMORY_FRAGMENTS)
        {
            System.out.println("[" + fragment_size(l) + ":" + fragment_pos(l) + "]");
        }
        System.out.println("-----------------------------------------");
    }

    public int fragment_size(long self)
    {
        return (int) (self);
    }

    public int fragment_pos(long self)
    {
        return (int) (self >> 32);
    }

    public static Runtime getRuntime() { return runtime; }

    public void gc()
    {
        for (Long l : REMV_MEMORY_FRAGMENTS) FREE_MEMORY_FRAGMENTS.remove(l);
        REMV_MEMORY_FRAGMENTS.clear();
    }

    private long getFree(int begins, int ends)
    {
        final long[] ret = {-1};

        FREE_MEMORY_FRAGMENTS.forEach(f ->
        {
            if (fragment_pos(f) == begins)
            {
                ret[0] = f;
            }
        });

        if(ret[0] == -1)
        {
            FREE_MEMORY_FRAGMENTS.forEach(f ->
            {
                if (fragment_pos(f) + fragment_size(f) == ends)
                {
                    ret[0] = f;
                }
            });
        }

        return ret[0];
    }

    private long getFreea(int ends)
    {
        final long[] ret = {-1};

        FREE_MEMORY_FRAGMENTS.forEach(f ->
        {
            if (fragment_pos(f) == ends)
            {
                ret[0] = f;
            }
        });

        return ret[0];
    }

    private long getFreeb(int ends)
    {
        final long[] ret = {-1};

        FREE_MEMORY_FRAGMENTS.forEach(f ->
        {
            if (fragment_pos(f) + fragment_size(f) == ends)
            {
                ret[0] = f;
            }
        });

        return ret[0];
    }

    private long getUsed(int ends)
    {
        final long[] ret = {-1};

        USED_MEMORY_FRAGMENTS.forEach(f->{
            if(fragment_pos(f) == ends)
            {
                ret[0] = f;
            }
        });

        return ret[0];
    }

    public void free(int ptr)
    {
        long pointer = getUsed(ptr);
        if(pointer < 0)
        {
            System.err.println("cannot destroy pointer [0x" + Integer.toHexString(fragment_pos(pointer)) + "] @" + fragment_pos(pointer));
            System.exit(0);
        }
        int end = fragment_pos(pointer) + fragment_size(pointer);
        boolean done = false;

        long free = getFreea(end);
//        long free = getFreea(fragment_pos(pointer));

        if(free > 0)
        {
            done = true;

            int size = fragment_size(free) + fragment_size(pointer);

            FREE_MEMORY_FRAGMENTS.remove(free);
            USED_MEMORY_FRAGMENTS.remove(pointer);
            FREE_MEMORY_FRAGMENTS.add(fragment(fragment_pos(pointer), size));
        } else {
            free = getFreeb(fragment_pos(pointer));

            if(free > 0)
            {
                done = true;

                int size = fragment_size(free) + fragment_size(pointer);

                FREE_MEMORY_FRAGMENTS.remove(free);
                USED_MEMORY_FRAGMENTS.remove(pointer);
                FREE_MEMORY_FRAGMENTS.add(fragment(fragment_pos(free), size));
            }
        }

        if(!done)
        {
            USED_MEMORY_FRAGMENTS.remove(pointer);
            FREE_MEMORY_FRAGMENTS.add(pointer);
        }
    }

    public int malloc(int amt)
    {
//        int amti = Memory.twiddle(amt);
        int amti = amt;

        final long[] POINTER = {0};

        FREE_MEMORY_FRAGMENTS.forEach(l->{
//            System.out.println(l + " " + fragment_size(l) + " " + amti);
            int size = 0;
            if((size = fragment_size(l)) >= amti)
            {
                FREE_MEMORY_FRAGMENTS.remove(l);
                int old_pos = fragment_pos(l);
                int old_sze = fragment_size(l);
//                System.out.println(old_pos + " " + old_sze);

                if(size > amti)
                {
                    long n = fragment(old_pos + amti, old_sze - amti);
                    FREE_MEMORY_FRAGMENTS.add(n);
                }

                USED_MEMORY_FRAGMENTS.add(POINTER[0] = fragment(old_pos, amti));
            }
        });

//        gc();

        return fragment_pos(POINTER[0]);
//        int size = STACK.popInt();
    }

    public int calloc(int amt)
    {
        int ptr = malloc(amt);
        for(int i = 0; i < amt; i ++) MEMORY[ptr + i] = 0;

        return ptr;
    }

    public
    /* struct */
    long
    fragment(int pos, int size)
    {
        long l = (((long) pos) << 32) | (size & 0xffffffffL);

//        fragments.add(l);
        return l;
    }
}