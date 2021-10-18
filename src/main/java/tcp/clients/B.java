package tcp.clients;

import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import general.algorithms.XXXAlgorithm;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class B extends Node implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public B(String ip, int port) throws IOException {
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
        var encryptionAlgorithm = getSingleMessage();

        if (encryptionAlgorithm.equals("ECB")) {
            algorithm = new ECBAlgorithm();
            sendMessage(MessagePrefix.MC, "k1");
        } else {
            algorithm = new XXXAlgorithm();
            sendMessage(MessagePrefix.MC, "k2");
        }

        print("Task " + currentTask + ':' + "   Read algorithm: '" + encryptionAlgorithm + "' from A and sent key request to MC.");
    }

    @Override
    public void task3() {
        voidTask();
    }

    @Override
    public void task4() {
        var encryptedKey = getSingleMessage();
        var decryptedKeyString = algorithm.decrypt(List.of(encryptedKey), K, iv);
        this.key = EncryptionAlgorithmAES.convertStringToSecretKey(decryptedKeyString);

        print("Task " + currentTask + ':' + "   Decrypted key '" + encryptedKey + "' into '" + EncryptionAlgorithmAES.convertSecretKeyToString(this.key) + "'.");
    }

    @Override
    public void task5() {
        sendMessage(MessagePrefix.A, "Start communication.");
        print("Task " + currentTask + ':' + "   Informed A to start communication.");
    }

    @Override
    public void task6() {
        voidTask();
    }

    @Override
    public void task7() {
        List<String> encryptedCipherTextList = getMessagesList();

        print("Decrypted the fallowing text:");
        var decryptedText = algorithm.decrypt(encryptedCipherTextList, key, iv);
        print(decryptedText);
        print("Task " + currentTask + ':' + "   Finished processing the crypto text.");
        System.out.flush();
    }

    @Override
    public void run() {
        start();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        new Thread(new B(ip, port)).start();
    }
}
