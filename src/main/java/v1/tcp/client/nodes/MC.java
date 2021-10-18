package v1.tcp.client.nodes;

import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import general.algorithms.XXXAlgorithm;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MC extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final SecretKey k1 = EncryptionAlgorithmAES.generateKey(128);
    private final SecretKey k2 = EncryptionAlgorithmAES.generateKey(128);
    private SecretKey key;

    public MC(String ip, int port) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        super(ip, port);
    }

    public void print(String msg) {
        System.out.println(ANSI_BLUE + "[MC]:    " + msg + ANSI_RESET);
    }

    @Override
    void loginIntoServer() {
        sendMessage(MessagePrefix.None, "MC");
        print(getLoginMessage());
    }

    @Override
    public void task1() {
        voidTask();
    }

    @Override
    public void task2() {
        voidTask();
//        var msg = getMessagesList().get(0);
//        print("Received msg. Will use the key for: " + msg);
//        if (msg.equals("ECB")) {
//            key = k1;
//            algorithm = new ECBAlgorithm();
//        } else {
//            key = k2;
//            algorithm = new XXXAlgorithm();
//        }

    }

    @Override
    public void task3() {
        voidTask();
//        print("Starting encryption algorithm for key.");
//        var encryptedBlockList = algorithm.encrypt(EncryptionAlgorithmAES.convertSecretKeyToString(key), K, iv);
//        var encryptedKey = encryptedBlockList.get(0);
//        print("Successfully encrypted key:'" + encryptedKey + "' with K:'" + EncryptionAlgorithmAES.convertSecretKeyToString(K) + "'.");
//        messenger.sendMessageToAMC(encryptedKey);
//        print("Sent encrypted key to A.");
//        messenger.sendMessageToBMC(encryptedKey);
//        print("Sent encrypted key to B.");
    }

    @Override
    public void task4() {
        voidTask();
        //do nothing
    }

    @Override
    public void task5() {
        voidTask();
        //do nothing
    }

    @Override
    public void task6() {
        voidTask();
        //do nothing
    }

    @Override
    public void task7() {
        voidTask();
        //do nothing
    }

    @Override
    public void run() {
        start();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        new Thread(new MC(ip, port)).start();
    }
}