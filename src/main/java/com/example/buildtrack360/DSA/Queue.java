package com.example.buildtrack360.DSA;

public class Queue<type> {
    private int front ;
    private int rare ;
    private int count;
    private int size;
    private type[] item;
    Queue(int itemNum)
    {
        size = itemNum;
        front = -1 ;
        rare =-1 ;
        count = 0;
        item = (type[]) new Object[size];
    }

    public boolean isEmpty()
    {
       if(count == 0)
       {
           return true;
       }
       return false;
    }
    public boolean isFull()
    {
        if(count == size)
        {
            return true;
        }
        return false;
    }
    public void enqueue(type data)
    {
        if(isFull())
        {
            System.out.println("queue overflow");
            return;
        }

        if(front == -1 && rare == -1)
        {
          front = rare = 0;
          item[rare] = data;
          count++;
        }
        else
        {
            rare = (rare + 1) % size;
            item[rare] = data;
            count++;
        }
    }

    public void dequeue()
    {
        if(isEmpty())
        {
            System.out.println("Queue Underflow");
            return;
        }
        if(front == rare)
        {
            front = -1;
            rare = -1;
            count = 0;
        }
        else
        {
            front = (front + 1) % size;
            count --;
        }
    }
    public void display()
    {
        if(isEmpty())
        {
            System.out.println("Queue is empty");
        }
        if(rare>=front)
        {
            for(int i=front;i<=rare;i++)
            {
                System.out.println(item[i]);
            }
        }
        else {
            for (int i = front; i < size; i++) {
                System.out.println(item[i]);
            }
            for (int i = 0; i <= rare; i++) {
                System.out.println(item[i]);
            }
        }
    }
    public int getCount()
    {
        return count;
    }
}
