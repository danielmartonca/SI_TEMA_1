package tcp.clients;

import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import general.algorithms.XXXAlgorithm;
import general.tasks.Tasks;

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

    public A(String ip, int port) throws IOException {
        super(ip, port);
    }

    private final String textFilePath = "src/main/resources/text_file.txt";


    void print(String msg) {
        System.out.println(ANSI_YELLOW + "[A]:     " + msg + ANSI_RESET);
    }

    void loginIntoServer() {
        sendMessage(MessagePrefix.None, "A");
        print(getLoginMessage());
    }

    @Override
    public void task1() {
        //TODO DELETE COMMENT
        //HARDCODE
        var encryptionAlgorithm = "ECB";
        algorithm = new ECBAlgorithm();


        //        var encryptionAlgorithm = readEncryptionMode();
        sendMessage(MessagePrefix.B, encryptionAlgorithm);

        if (ECBAlgorithm.class.equals(algorithm.getClass())) {
            sendMessage(MessagePrefix.MC, "k1");
        } else if (XXXAlgorithm.class.equals(algorithm.getClass())) {
            sendMessage(MessagePrefix.MC, "k2");
        }

        print("Task " + currentTask + ':' + "   Read algorithm: '" + encryptionAlgorithm + "' from user and sent key request to MC.");
    }

    @Override
    public void task2() {
        voidTask();
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
        voidTask();
    }

    @Override
    public void task6() {
        var msg = getSingleMessage();
        if (msg.equalsIgnoreCase("Start communication.")) {
            print("Task " + currentTask + ':' + "   Received signal '" + msg + "'.     Starting sending blocks of encrypted text.");
        } else {
            print("Task " + currentTask + ':' + "   ERROR! DID NOT RECEIVE START COMMUNICATION SIGNAL.");
            stopConnection();
        }
    }

    @Override
    public void task7() {
        List<String> textFileLinesList = getListOfTextFileLines();
        print("Sending encrypted text to B.");
        System.out.flush();
        for (var line : textFileLinesList) {
            var encryptedBlockList = algorithm.encrypt(line, key, iv);
            for (var encryptedBlock : encryptedBlockList)
                sendMessage(MessagePrefix.B, encryptedBlock);
        }
        print("Task " + currentTask + ':' + "   Sent the crypto text towards B.");
    }


    private List<String> getListOfTextFileLines() {
        List<String> list = new LinkedList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(this.textFilePath));
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

    private String readEncryptionMode() {
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

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        new Thread(new A(ip, port)).start();
    }

    @Override
    public void run() {
        start();
    }
}
