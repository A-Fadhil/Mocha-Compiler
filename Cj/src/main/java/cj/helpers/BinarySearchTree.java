package cj.helpers;

import cj.Token;
import cj.TokenStream;

public class BinarySearchTree<E>
{
    Node<E> list;
    int  max;
    int  ind;

    public BinarySearchTree(int max)
    {
        this.max = max;
        list = new Node<E>(max);//[max];
//        for(int i = 0; i < max; i ++) list[i] = new Node(max);
    }

    public <E> E get(int pointer)
    {
        Node parent = list;
        for(int i = 0; i < max; i ++)
        {
            parent = ((pointer >> i) & 1) == 0 ? parent.left : parent.right;
        }

        return (E) parent.E;
    }

    public void add(int E)
    {
        set(ind ++, E);
    }

    public <E>E set(int pointer, int set)
    {
        Node parent = list;
        for(int i = 0; i < max; i ++)
        {
            parent = ((pointer >> i) & 1) == 0 ? parent.left : parent.right;
        }

        parent.E = set;

        return (E) parent.E;
    }

    public int total() { return Node.total; }
}

class Node<E>{
    static int total;
    Node left;
    Node right;
    E  E;

    public Node(int size)
    {
        total = total + 1;
        if(size == 0) return;
        left  = new Node(size - 1);
        right = new Node(size - 1);
    }
//
//    public E left() { return (E) left.E; }
//    public E right() { return (E) right.E; }

    public <E>E left() { return (E) left.E; }
    public <E>E right() { return (E) right.E; }

    public <E>E left(int E) { left.E = E; return (E) left.E; }
    public <E>E right(int E) { right.E = E; return (E) right.E; }

    public void left(Node left)
    {
        this.left = left;
    }

    public void right(Node right)
    {
        this.right = right;
    }
}