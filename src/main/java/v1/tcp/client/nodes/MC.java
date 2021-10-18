package v1.tcp.client.nodes;

import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import general.algorithms.XXXAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
    }

    @Override
    public void task3() {
        var messagesList = getMessagesList();
        for (var message : messagesList) {
            if (message.equals("k1")) {
                algorithm = new ECBAlgorithm();
                key = k1;
            } else {
                algorithm = new XXXAlgorithm();
                key = k2;
            }
            break;
        }

        var encryptedBlockList = algorithm.encrypt(EncryptionAlgorithmAES.convertSecretKeyToString(key), K, iv);
        var encryptedKey = encryptedBlockList.get(0);

        sendMessage(MessagePrefix.A, encryptedKey);
        sendMessage(MessagePrefix.B, encryptedKey);

        print("Task " + currentTask + ':' + "  Successfully encrypted key:'" + encryptedKey + "' with K:'" + EncryptionAlgorithmAES.convertSecretKeyToString(K) + "' and sent it to A and B.");
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