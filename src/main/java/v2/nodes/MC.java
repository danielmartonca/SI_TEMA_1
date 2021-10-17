package v2.nodes;

import v2.algorithms.EncryptionAlgorithmAES;
import v2.messenger.Messenger;
import v2.algorithms.ECBAlgorithm;
import v2.algorithms.XXXAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

public class MC extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final SecretKey k1 = EncryptionAlgorithmAES.generateKey(128);
    private final SecretKey k2 = EncryptionAlgorithmAES.generateKey(128);
    private SecretKey key;
    private final IvParameterSpec iv;

    public MC(Messenger messenger, IvParameterSpec iv) throws NoSuchAlgorithmException {
        super(messenger);
        this.iv = iv;
    }

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


    public void print(String msg) {
        System.out.println(ANSI_BLUE + "[MC]:    " + msg + ANSI_RESET);
    }

    @Override
    public void task1() throws InterruptedException {
        waitForSignal();
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
        var encryptedKey = algorithm.customEncrypt(EncryptionAlgorithmAES.convertSecretKeyToString(key), K, iv);
        print("Successfully encrypted key:'" + encryptedKey + "' with K:'" + EncryptionAlgorithmAES.convertSecretKeyToString(K) + "'.");
        messenger.sendMessageToAMC(encryptedKey);
        print("Sent encrypted key to A.");
        messenger.sendMessageToBMC(encryptedKey);
        print("Sent encrypted key to B.");
        Messenger.setMCIsWaiting(false);
        Messenger.setAIsWaiting(false);
        Messenger.setBIsWaiting(false);
    }

    @Override
    public void task4() {
        //do nothing
    }

    @Override
    public void task5() {
        //do nothing
    }

    @Override
    public void task6() {
        //do nothing
    }

    @Override
    public void task7() {
        //do nothing
    }


    private void waitForSignal() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.mcIsWaiting)
            Thread.sleep(1000);
    }
}