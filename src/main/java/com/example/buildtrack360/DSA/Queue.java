package com.example.buildtrack360.DSA;

public class Queue<type> {
    private int front ;
    private int rare ;
    private int count;
    private int size;
    private type[] item;
    public Queue(int itemNum)
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
            resize(size * 2);
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

    public type dequeue()
    {
        if(isEmpty())
        {
            System.out.println("Queue Underflow");
            return null;
        }
        if(front == rare)
        {
            type temp= item[front];
            front = -1;
            rare = -1;
            count = 0;
            return temp;
        }
        else
        {
            type temp= item[front];
            front = (front + 1) % size;
            count --;
            return  temp;
        }
    }

    private void resize(int newSize) {
        type[] newItemArray = (type[]) new Object[newSize];
        int index = 0;

        // Copy elements from the old array to the new one
        if (front <= rare) {
            // Elements are in a continuous block in the old array
            for (int i = front; i <= rare; i++) {
                newItemArray[index++] = item[i];
            }
        } else {
            // Elements are split across the end of the old array and the beginning
            for (int i = front; i < size; i++) {
                newItemArray[index++] = item[i];
            }
            for (int i = 0; i <= rare; i++) {
                newItemArray[index++] = item[i];
            }
        }

        // Update front and rear indices
        front = 0;
        rare = count - 1;
        size = newSize;
        item = newItemArray;
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
