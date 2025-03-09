package com.example.buildtrack360.DSA;

import com.example.buildtrack360.Backend.Roles;
public class LinkedList<type> {
    int count=0;
    private Node<type> head=null;
    private Node<type> tail=null;
    public Node<type> tempNode=null;
    public Node<type> GetHead(){
        return head;
    }
    public void InsertData(type data) {
        try {
            Node<type> newnode = new Node<type>();
            if (head == null&&tail==null) {

                newnode.data = data;
                head = newnode;
                tail=newnode;
                count++;
                System.out.println("Successfully Added a new head node: " + newnode.data);
                return;
            }
            newnode.data = data;            tail.next = newnode;
            newnode.previous = tail;
            tail=newnode;
            count++;
            System.out.println("Successfully Added a new node: " + newnode.data);
        } catch (Exception e) {
            System.out.println("Exception in LinkedList (line 26) : " + e.getMessage());
        }
    }
    public int ListSize(){return count;}
    public Node<type> DeleteData(type data){
        if (head == null) {
            System.out.println("No data to remove.");
            return null;
        }
        Node<type> tempnode = head;

        // Deleting the head node
        if (head.data.equals(data)) {
            head = head.next;
            if (head != null) {
                head.previous = null;
            } else {
                tail = null;
            }
            count--;
            return tempnode;
        }

        // Deleting other nodes
        while (tempnode != null) {
            if (tempnode.data.equals(data)) {
                if (tempnode.next != null) {
                    tempnode.next.previous = tempnode.previous;
                } else {
                    tail = tempnode.previous;
                }
                if (tempnode.previous != null) {
                    tempnode.previous.next = tempnode.next;
                }
                count--;
                return tempnode;
            }
            tempnode = tempnode.next;
        }

        System.out.println("Data not found in the list.");
        return null;
    }
    // 📌 Search by Name (Specifically for Roles objects)
    public Integer searchByName(String name) {
        Node<type> current = head;
        while (current != null) {
            if (current.data instanceof Roles) {
                Roles role = (Roles) current.data; // Cast to Roles
                if (role.GetName().equals(name)) {
                    return role.GetID(); // Return the ID if the name matches
                }
            }
            current = current.next;
        }
        System.out.println("Role with name '" + name + "' not found.");
        return null; // Return null if not found
    }
}

