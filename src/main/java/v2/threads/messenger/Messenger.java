package v2.threads.messenger;

import java.util.Vector;

public class Messenger {

    // initialization of queue size
    private static final int MAX = 1;
    private static final Vector<String> messagesAB = new Vector<>();
    private static final Vector<String> messagesAMC = new Vector<>();
    private static final Vector<String> messagesBMC = new Vector<>();


    public static boolean aIsWaiting = false;
    public static boolean bIsWaiting = true;
    public static boolean mcIsWaiting = true;

    public static void setAIsWaiting(boolean aIsWaiting) {
        Messenger.aIsWaiting = aIsWaiting;
    }

    public static void setBIsWaiting(boolean bIsWaiting) {
        Messenger.bIsWaiting = bIsWaiting;
    }

    public static void setMCIsWaiting(boolean mcIsWaiting) {
        Messenger.mcIsWaiting = mcIsWaiting;
    }


    public synchronized void sendMessageToAB(String message) throws InterruptedException {

        // checks whether the queue is full or not
        while (Messenger.messagesAB.size() == MAX)
            // waits for the queue to get empty
            wait();

        Messenger.messagesAB.add(message);
        notify();
    }

    public synchronized String getMessageFromAB() throws InterruptedException {

        notify();
        while (Messenger.messagesAB.size() == 0)
            wait();
        String message = Messenger.messagesAB.firstElement();

        // extracts the message from the queue
        Messenger.messagesAB.removeElement(message);
        return message;
    }

    public synchronized void sendMessageToAMC(String message) throws InterruptedException {

        // checks whether the queue is full or not
        while (Messenger.messagesAMC.size() == MAX)
            // waits for the queue to get empty
            wait();

        Messenger.messagesAMC.add(message);
        notify();
    }

    public synchronized String getMessageFromAMC() throws InterruptedException {

        notify();
        while (Messenger.messagesAMC.size() == 0)
            wait();
        String message = Messenger.messagesAMC.firstElement();

        // extracts the message from the queue
        Messenger.messagesAMC.removeElement(message);
        return message;
    }

    public synchronized void sendMessageToBMC(String message) throws InterruptedException {

        // checks whether the queue is full or not
        while (Messenger.messagesBMC.size() == MAX)
            // waits for the queue to get empty
            wait();

        Messenger.messagesBMC.add(message);
        notify();
    }

    public synchronized String getMessageFromBMC() throws InterruptedException {

        notify();
        while (Messenger.messagesBMC.size() == 0)
            wait();
        String message = Messenger.messagesBMC.firstElement();

        // extracts the message from the queue
        Messenger.messagesBMC.removeElement(message);
        return message;
    }


}
