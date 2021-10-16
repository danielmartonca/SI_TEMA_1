package v2.nodes;

import v2.Messenger;
import v2.algorithms.ECBAlgorithm;
import v2.algorithms.XXXAlgorithm;

public class B extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private String encryptionAlgorithm;

    public B(Messenger messenger) {
        super(messenger);
    }

    @Override
    public void run() {
        print("Started");
        try {
            while (currentTask <= 7) {
                doTask();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String msg) {
        System.out.println(ANSI_PURPLE + "[B]:    " + msg + ANSI_RESET);
    }

    private void requestKeyFromMC(String encryptionAlgorithm) throws InterruptedException {
        if (encryptionAlgorithm.equals("ECB")) {
            algorithm = new ECBAlgorithm();
            messenger.sendMessageToBMC("k1");
            print("Sent request to MC for key1");
        } else {
            algorithm = new XXXAlgorithm();
            messenger.sendMessageToBMC("k2");
            print("Sent request to MC for key2");
        }

    }

    @Override
    public void task1() throws InterruptedException {
        noTask();
    }

    @Override
    public void task2() throws InterruptedException {
        var encryption = messenger.getMessageFromAB();
        print("Received " + encryption + " from A.");
        requestKeyFromMC(encryption);
        Messenger.setAIsWaiting(true);
    }

    @Override
    public void task3() throws InterruptedException {
        noTask();
    }

    @Override
    public void task4() throws InterruptedException {
        var encryptedKey = messenger.getMessageFromAMC();
        print("Received encrypted key: " + encryptedKey);
    }

    @Override
    public void task5() {

    }

    @Override
    public void task6() {

    }

    @Override
    public void task7() {

    }

    @Override
    public void quit() {

    }

    private void noTask() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.bIsWaiting)
            Thread.sleep(5000);
    }
}
