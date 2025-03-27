package seedu.address.model;

import static java.util.Objects.requireNonNull;


/**
 * Custom DoublyLinkedList (DLL) to keep track of past user inputs
 */
public class DoublyLinkedList {

    /**
     * Node serves as inner class to store the user inputs as individual objects
     */
    class Node {
        private String data;
        private Node prev;
        private Node next;

        public Node(String data) {
            requireNonNull(data);
            this.data = data;
            this.prev = null;
            this.next = null;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof Node)) {
                return false;
            }

            Node otherNode = (Node) other;

            // Check only the data contained within the node to prevent infinite loops
            return data.equals(otherNode.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }
    }

    private Node head;
    private Node tail;
    private Node curr; // Pseudo pointer to current position in the DLL
    private int size;

    /**
     * Creates a DDL with a dummy tail node.
     */
    public DoublyLinkedList() {
        this.head = null;
        // Dummy tail node
        this.tail = new Node("");
        this.curr = null;
        this.size = 0;
    }

    /**
     * Adds a new node to the end of the DLL
     *
     * @param command command string that user had input
     */
    public void add(String command) {
        Node temp = new Node(command);
        // If empty DDL, set head as new node
        if (head == null) {
            head = temp;
            head.next = tail;
            tail.prev = head;
            curr = tail;
            size = 1;
        } else {
            Node dummy = tail.prev;
            // Set link between temp and tail
            tail.prev = temp;
            temp.next = tail;
            // Set link between temp and tail.prev
            temp.prev = dummy;
            dummy.next = temp;
            size += 1;
        }
    }

    /**
     * Resets the current pointer to point to the tail of the DDL
     */
    public void reset() {
        curr = tail;
    }

    /**
     * Moves current pointer to point to the previous node
     */
    public void moveBack() {
        // Decrement only if current pointer is not at the head
        if (curr.prev != null) {
            curr = curr.prev;
        }
    }

    /**
     * Moves current pointer to point to the next node
     */
    public void moveForward() {
        // Increment only if current pointer is not at the tail
        if (curr.next != null) {
            curr = curr.next;
        }
    }

    public String getCommand() {
        return curr.data;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DoublyLinkedList)) {
            return false;
        }

        DoublyLinkedList otherDoublyLinkedList = (DoublyLinkedList) other;

        if (this.size != otherDoublyLinkedList.size) {
            return false;
        }

        Node tempA = this.head;
        Node tempB = otherDoublyLinkedList.head;

        // Check each node to ensure equality in data and order
        while (tempA != null) {
            if (!tempA.equals(tempB)) {
                return false;
            }
            tempA = tempA.next;
            tempB = tempB.next;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        Node temp = this.head;
        while (temp != null) {
            // To ensure that order is taken into consideration for hash
            hash = 37 * hash + temp.data.hashCode();
            temp = temp.next;
        }
        return hash;
    }
}
