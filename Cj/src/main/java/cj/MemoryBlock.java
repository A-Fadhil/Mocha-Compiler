package cj;

public class MemoryBlock
{
    byte block[];
    byte block_escaped[];
    boolean reserved;
    int reservedb;

    public MemoryBlock(int size)
    {
        block = new byte[size];
        block_escaped = new byte[size];
    }

    public void reserve()
    {
        reserved = true;
    }

    public boolean available() { return !reserved; }
    public int     availablebytes() { return block.length - reservedb; }
}