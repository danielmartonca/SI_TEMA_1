package v2.nodes;

import v2.Messenger;
import v2.algorithms.ECBAlgorithm;
import v2.algorithms.XXXAlgorithm;

public class MC extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final int k1 = 1;
    private final int k2 = 2;
    private final int K = 3;
    private int key;

    public MC(Messenger messenger) {
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
        System.out.println(ANSI_BLUE + "[MC]:    " + msg + ANSI_RESET);
    }

    @Override
    public void task1() throws InterruptedException {
        noTask();
    }

    @Override
    public void task2() throws InterruptedException {
        var msg = messenger.getMessageFromAMC();
        print("Received msg. Will use the key for: " + msg);
        if (msg.equals("ECB")) {
            key = k1;
            algorithm = new ECBAlgorithm();
        } else {
            key = k2;
            algorithm = new XXXAlgorithm();
        }
        Messenger.setMCIsWaiting(false);
        Messenger.setAIsWaiting(true);
        Messenger.setBIsWaiting(true);
    }

    @Override
    public void task3() throws InterruptedException {
        print("Starting encryption algorithm for key.");
        var encryptedKey = algorithm.encrypt(String.valueOf(key), String.valueOf(K));
        print("Successfully encrypted key:" + encryptedKey);
        messenger.sendMessageToAMC(encryptedKey);
        print("Sent key to A.");
        messenger.sendMessageToBMC(encryptedKey);
        print("Sent key to B.");
        Messenger.setMCIsWaiting(true);
        Messenger.setAIsWaiting(false);
        Messenger.setBIsWaiting(false);
    }

    @Override
    public void task4() throws InterruptedException {
        noTask();
    }

    @Override
    public void task5() throws InterruptedException {

    }

    @Override
    public void task6() throws InterruptedException {

    }

    @Override
    public void task7() throws InterruptedException {

    }

    @Override
    public void quit() {

    }

    private void noTask() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.mcIsWaiting)
            Thread.sleep(5000);
    }
}