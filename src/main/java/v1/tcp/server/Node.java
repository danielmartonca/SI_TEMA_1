package v1.tcp.server;

import java.util.Vector;

public class Node {
    private final String password;
    private boolean isAssigned;
    private final Vector<String> messagesVector = new Vector<>();

    public Node(String password) {
        this.password = password;
        this.isAssigned = false;
    }

    String getPassword() {
        return password;
    }


    boolean isAssigned() {
        return isAssigned;
    }

    void assignNode() {
        this.isAssigned = true;
    }


    void sendMessage(String message) {
        synchronized (messagesVector) {
            messagesVector.add(message);
        }
    }

    String getMessage() {
        String message;
        synchronized (messagesVector) {
            if (messagesVector.size() == 0) return null;
            message = messagesVector.firstElement();
            // extracts the message from the queue
            messagesVector.removeElement(message);
        }
        return message;
    }
}
