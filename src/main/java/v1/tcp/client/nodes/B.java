package v1.tcp.client.nodes;

import general.algorithms.ECBAlgorithm;
import general.algorithms.XXXAlgorithm;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class B extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private SecretKey key;

    public B(String ip, int port) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        super(ip, port);
    }

    void print(String msg) {
        System.out.println(ANSI_PURPLE + "[B]:     " + msg + ANSI_RESET);
    }

    @Override
    void loginIntoServer() {
        sendMessage(MessagePrefix.None, "B");
        print(getLoginMessage());
    }

    @Override
    public void task1() {
        voidTask();
    }

    @Override
    public void task2() {
        voidTask();
        var encryptionAlgorithm = getSingleMessage();

        if (encryptionAlgorithm.equals("ECB")) {
            algorithm = new ECBAlgorithm();
            sendMessage(MessagePrefix.MC, "k1");
            print("Sent request to MC for key1");
        } else {
            algorithm = new XXXAlgorithm();
            sendMessage(MessagePrefix.MC, "k2");
            print("Sent request to MC for key2");
        }

        print("Task " + currentTask + ':' + "   Read algorithm: '" + encryptionAlgorithm + "' from A and sent key request to MC.");
    }

    @Override
    public void task3() {
        voidTask();
    }

    @Override
    public void task4() {
        voidTask();
//        var encryptedKey = messenger.getMessageFromBMC();
//        print("Received encrypted key: " + encryptedKey);
//        var decryptedKeyString = algorithm.decrypt(List.of(encryptedKey), K, iv);
//        this.key = EncryptionAlgorithmAES.convertStringToSecretKey(decryptedKeyString);
//        print("Decrypted key '" + encryptedKey + "' into '" + EncryptionAlgorithmAES.convertSecretKeyToString(this.key) + "'.");
//
    }

    @Override
    public void task5() {
        voidTask();
//        messenger.sendMessageToAB("Start");
//        print("Sent signal to A to start sending encrypted text.");
    }

    @Override
    public void task6() {
        voidTask();
    }

    @Override
    public void task7() {
        voidTask();
//        
//        messenger.getMessageFromAB();
//        String message;
//        List<String> encryptedCipherTextList = new LinkedList<>();
//        do {
//            message = messenger.getMessageFromAB();
//            encryptedCipherTextList.add(message);
//        } while (!message.equals("END"));
//
//        print("Decrypted the fallowing text:");
//        var decryptedText = algorithm.decrypt(encryptedCipherTextList, key, iv);
//        print(decryptedText);
//        System.out.flush();

    }

    @Override
    public void run() {
        start();
    }

    private void requestKeyFromMC(String encryptionAlgorithm) {

    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        new Thread(new B(ip, port)).start();
    }
}
