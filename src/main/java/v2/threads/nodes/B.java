package v2.threads.nodes;

import general.algorithms.EncryptionAlgorithmAES;
import v2.threads.messenger.Messenger;
import general.algorithms.ECBAlgorithm;
import general.algorithms.XXXAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class B extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public B(Messenger messenger, IvParameterSpec iv) throws NoSuchAlgorithmException {
        super(messenger);
        this.iv = iv;
    }

    private SecretKey key;
    private final IvParameterSpec iv;


    @Override
    public void run() {
        print("Started");
        try {
            while (currentTask <= 8) {
                doTask();
                System.out.flush();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void print(String msg) {
        System.out.println(ANSI_PURPLE + "[B]:     " + msg + ANSI_RESET);
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
        waitForSignal();
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
        waitForSignal();
    }

    @Override
    public void task4() throws InterruptedException {
        var encryptedKey = messenger.getMessageFromBMC();
        print("Received encrypted key: " + encryptedKey);
        var decryptedKeyString = algorithm.decrypt(List.of(encryptedKey), K, iv);
        this.key = EncryptionAlgorithmAES.convertStringToSecretKey(decryptedKeyString);
        print("Decrypted key '" + encryptedKey + "' into '" + EncryptionAlgorithmAES.convertSecretKeyToString(this.key) + "'.");


        while (!Messenger.aIsWaiting) Thread.sleep(1000);
        Messenger.setAIsWaiting(true);
        Messenger.setBIsWaiting(false);
        Messenger.setMCIsWaiting(true);
    }

    @Override
    public void task5() throws InterruptedException {
        messenger.sendMessageToAB("Start");
        print("Sent signal to A to start sending encrypted text.");
        Messenger.setAIsWaiting(false);
        Messenger.setBIsWaiting(true);
    }

    @Override
    public void task6() throws InterruptedException {
        waitForSignal();
    }

    @Override
    public void task7() throws InterruptedException {
        while (Messenger.bIsWaiting)
            Thread.sleep(1000);

        messenger.getMessageFromAB();
        String message;
        List<String> encryptedCipherTextList = new LinkedList<>();
        do {
            message = messenger.getMessageFromAB();
            encryptedCipherTextList.add(message);
        } while (!message.equals("END"));

        print("Decrypted the fallowing text:");
        var decryptedText = algorithm.decrypt(encryptedCipherTextList, key, iv);
        print(decryptedText);
        System.out.flush();

    }

    @Override
    public void quit() {
        if (currentTask > 7)
            print("Thread finished action.");
    }

    private void waitForSignal() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.bIsWaiting)
            Thread.sleep(1000);

    }
}
