package v2.nodes;

import v2.Messenger;
import v2.algorithms.ECBAlgorithm;
import v2.algorithms.EncryptionAlgorithm;
import v2.algorithms.XXXAlgorithm;
import v2.tasks.Tasks;

import java.util.Scanner;

public class A extends Node implements Runnable, Tasks {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public A(Messenger messenger) {
        super(messenger);
    }

    private void print(String msg) {
        System.out.println(ANSI_YELLOW + "[A]:    " + msg + ANSI_RESET);
    }

    @Override
    public void run() {
        print("Started");
        try {
            while (currentTask <= 7) {
                doTask();
                System.out.flush();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        print("Closing thread.");
    }

    private String readEncryptionMode() {
        Messenger.setAIsWaiting(false);
        print("Enter an encryption algorithm: ECB,XXX:  ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            var algorithm = scanner.nextLine();
            switch (algorithm.toUpperCase()) {
                case "ECB" -> {
                    this.algorithm = new ECBAlgorithm();
                    return "ECB";
                }
                case "XXX" -> {
                    this.algorithm = new XXXAlgorithm();
                    return "XXX";
                }
                default -> print("Invalid algorithm. Please enter one of the fallowing: ECB, XXX    " + algorithm);
            }
        }

    }

    private void sendEncryptionModeToB(String encryptionMode) throws InterruptedException {
        messenger.sendMessageToAB(encryptionMode);
    }

    private void requestKeyFromMC(EncryptionAlgorithm algorithm) throws InterruptedException {
        if (algorithm.getClass() == ECBAlgorithm.class)
            messenger.sendMessageToAMC("ECB");
        else
            messenger.sendMessageToAMC("XXX");
    }


    @Override
    public void task1() throws InterruptedException {
        if (currentTask != 1) return;
        var encryptionMode = readEncryptionMode();
        sendEncryptionModeToB(encryptionMode);
        requestKeyFromMC(algorithm);

        Messenger.setAIsWaiting(true);
        Messenger.setBIsWaiting(false);
        Messenger.setMCIsWaiting(false);
    }

    @Override
    public void task2() throws InterruptedException {
        noTask();
    }

    @Override
    public void task3() throws InterruptedException {
        noTask();
    }

    @Override
    public void task4() throws InterruptedException {
        var encryptedKey = messenger.getMessageFromAMC();
        print("Received encrypted key: " + encryptedKey);

        Messenger.setAIsWaiting(true);
    }

    @Override
    public void task5() throws InterruptedException {
        noTask();
    }

    @Override
    public void task6() {
    }

    @Override
    public void task7() {
    }

    @Override
    public void quit() {
        if (currentTask > 7)
            print("Thread finished action.");
    }

    private void noTask() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.aIsWaiting)
            Thread.sleep(1000);
    }
}
