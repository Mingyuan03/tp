package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

/**
 * Custom DoublyLinkedList (DLL) to keep track of past user inputs
 * Implementation uses ArrayList for efficient access
 */
public class DoublyLinkedList {
    private ArrayList<String> commands;
    private int currIndex; // Index representing current position

    /**
     * Creates a new command history tracker
     */
    public DoublyLinkedList() {
        this.commands = new ArrayList<>();
        this.currIndex = -1; // No current position when empty
    }

    /**
     * Adds a new command to the history
     *
     * @param command command string that user had input
     */
    public void add(String command) {
        requireNonNull(command);
        commands.add(command);
        reset(); // Reset curr to point to the "end" position
    }

    /**
     * Resets the current pointer to point to the position after the last command
     * This matches the original behavior where reset pointed to the dummy tail
     */
    public void reset() {
        currIndex = commands.size();
    }

    /**
     * Moves current pointer to point to the previous command
     */
    public void moveBack() {
        // Decrement only if not at the beginning
        if (currIndex > 0) {
            currIndex--;
        }
    }

    /**
     * Moves current pointer to point to the next command
     */
    public void moveForward() {
        // Increment only if not past the end
        if (currIndex < commands.size()) {
            currIndex++;
        }
    }

    /**
     * Gets the command at current position
     * Returns empty string if at the "tail" position (past the end)
     */
    public String getCommand() {
        // Return empty string if at the virtual "tail" position
        if (currIndex == commands.size() || commands.isEmpty()) {
            return "";
        }
        return commands.get(currIndex);
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

        return this.commands.equals(otherDoublyLinkedList.commands);
    }

    @Override
    public int hashCode() {
        return commands.hashCode();
    }
}
