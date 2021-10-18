package v1.tcp.client.nodes;

import general.algorithms.EncryptionAlgorithmAES;
import general.tasks.Tasks;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public abstract class Node implements Tasks {
    public static final String ip = "127.0.0.1";
    public static final int port = 6666;

    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    protected int currentTask = 1;
    protected EncryptionAlgorithmAES algorithm;
    protected SecretKey K;          //=EncryptionAlgorithmAES.generateKey(128);
    protected IvParameterSpec iv;   //=EncryptionAlgorithmAES.generateIV();

    protected Node(String ip, int port) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        print("Found connection successfully.");
    }


    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            print("FAILED TO CLOSE CONNECTIONS");
        }
    }


    private String parseMessage(MessagePrefix prefix, String message) {
        StringBuilder p = new StringBuilder();
        switch (prefix) {
            case Read -> p.append("[R]").append(message);
            case A -> p.append("[A]").append(message);
            case B -> p.append("[B]").append(message);
            case MC -> p.append("[MC]").append(message);
            case FinishedTask -> p.append("[F]").append(message);
            case None -> p.append(message);
        }
        return p.toString();
    }

    protected void sendMessage(MessagePrefix prefix, String message) {
        out.println(parseMessage(prefix, message));
    }

    private void sendFlagToStartGivingMessages() {
        sendMessage(MessagePrefix.Read, "");
    }

    private boolean ifItIsFlagToStopReadingMessages(String receivedMessage) {
        return receivedMessage.startsWith("[D]");    //message retrieval stops when [D] flag is received
    }


    protected List<String> getMessagesList() {
        try {
            sendFlagToStartGivingMessages();

            String receivedMessage;
            List<String> receivedMessagesList = new LinkedList<>();
            while (true) {
                receivedMessage = in.readLine();

                if (ifItIsFlagToStopReadingMessages(receivedMessage))
                    break;

                receivedMessagesList.add(receivedMessage);
            }
            return receivedMessagesList;
        } catch (IOException e) {
            e.printStackTrace();
            print("FAILED TO READ FROM THE SERVER.");
        }
        return null;
    }

    protected String getSingleMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAILED TO READ MESSAGE";
    }


    protected void getServerFlag() {
        var flag = getSingleMessage();//get flag to start task
        if (!flag.equals("[START TASK]")) {
            print("ERROR WHILE READING FLAG TO START TASK. GOT:" + flag);
            System.out.flush();
            stopConnection();
        }
    }

    protected void sendFlagToServer() {
        sendMessage(MessagePrefix.FinishedTask, "");
    }

    protected void doTask() {
        try {
            getServerFlag();//get flag to start task

            print("Next task: " + currentTask);

            if (currentTask == 8) {
                quit();
                currentTask++;
                return;
            }

            switch (currentTask) {
                case 1 -> task1();
                case 2 -> task2();
                case 3 -> task3();
                case 4 -> task4();
                case 5 -> task5();
                case 6 -> task6();
                case 7 -> task7();
            }

            Thread.sleep(1000);
            currentTask++;

            sendFlagToServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
            print("SLEEP FAILED");
        }
    }

    protected void start() {
        print("Started");
        loginIntoServer();
        print("Waiting for other users to connect.");

        while (currentTask <= 8) {
            doTask();
            System.out.flush();
        }
    }

    protected void quit() {
        if (currentTask > 7)
            print("Thread finished action.");
        stopConnection();
    }

    protected void voidTask() {
        try {
            print("No task to do at step " + currentTask + '.');
//            sendMessage(MessagePrefix.None, "");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            print("SLEEP FAILED");
        }
    }

    abstract void loginIntoServer();

    abstract void print(String msg);
}
