package v2.nodes;

import v2.messenger.Messenger;
import v2.algorithms.ECBAlgorithm;
import v2.algorithms.EncryptionAlgorithmAES;
import v2.algorithms.XXXAlgorithm;
import v2.tasks.Tasks;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class A extends Node implements Runnable, Tasks {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public A(Messenger messenger, IvParameterSpec iv) throws NoSuchAlgorithmException {
        super(messenger);
        this.iv = iv;
    }

    private final String filePath = "src/main/resources/text_file.txt";
    private SecretKey key = null;
    private final IvParameterSpec iv;


    void print(String msg) {
        System.out.println(ANSI_YELLOW + "[A]:     " + msg + ANSI_RESET);
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
            System.out.println(e.getMessage());
        }
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

    private void requestKeyFromMC(EncryptionAlgorithmAES algorithm) throws InterruptedException {
        if (algorithm.getClass() == ECBAlgorithm.class)
            messenger.sendMessageToAMC("ECB");
        else
            messenger.sendMessageToAMC("XXX");
    }

    @Override
    public void task1() throws InterruptedException {
        var encryptionMode = readEncryptionMode();
        sendEncryptionModeToB(encryptionMode);
        requestKeyFromMC(algorithm);

        Messenger.setAIsWaiting(true);
        Messenger.setBIsWaiting(false);
        Messenger.setMCIsWaiting(false);
    }

    @Override
    public void task2() throws InterruptedException {
        waitForSignal();
    }

    @Override
    public void task3() throws InterruptedException {
        waitForSignal();
    }

    @Override
    public void task4() throws InterruptedException {
        var encryptedKey = messenger.getMessageFromAMC();
        print("Received encrypted key: " + encryptedKey);
        var decryptedKeyString = algorithm.decrypt(List.of(encryptedKey), K, iv);
        this.key = EncryptionAlgorithmAES.convertStringToSecretKey(decryptedKeyString);
        print("Decrypted key '" + encryptedKey + "' into '" + EncryptionAlgorithmAES.convertSecretKeyToString(this.key) + "'.");

        Messenger.setAIsWaiting(false);
        Messenger.setMCIsWaiting(true);
    }

    @Override
    public void task5() throws InterruptedException {
        waitForSignal();
    }

    @Override
    public void task6() throws InterruptedException {
        var msg = messenger.getMessageFromAB();
        if (msg.equalsIgnoreCase("START")) {
            print("Received signal '" + msg + "'.     Starting sending blocks of encrypted text.");
            Messenger.setAIsWaiting(false);
            Messenger.setBIsWaiting(false);
            return;
        }
        print("Received '" + msg + "'. UNKNOWN");
        Messenger.setAIsWaiting(false);
    }

    @Override
    public void task7() throws InterruptedException {
        List<String> textFileLinesList = getListOfTextFileLines();
        print("Sending encrypted text to B.");
        System.out.flush();
        for (var line : textFileLinesList) {
            var encryptedBlockList = algorithm.encrypt(line, key, iv);
            for (var encryptedBlock : encryptedBlockList)
                messenger.sendMessageToAB(encryptedBlock);
        }

        messenger.sendMessageToAB("END");
    }

    private List<String> getListOfTextFileLines() {
        List<String> list = new LinkedList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(this.filePath));
            var line = in.readLine();
            while (line != null) {
                list.add(line);
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            print("ERROR!!! COULDN'T FIND FILE");
            e.printStackTrace();
        } catch (IOException ioException) {
            print("ERROR!!! FOUND FILE BUT COULDN'T READ FROM IT");
            ioException.printStackTrace();
        }
        return list;
    }

    @Override
    public void quit() {
        if (currentTask > 7)
            print("Thread finished action.");
    }

    private void waitForSignal() throws InterruptedException {
        print("No task at the moment.");
        while (Messenger.aIsWaiting)
            Thread.sleep(1000);
    }
}
