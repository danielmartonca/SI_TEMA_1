package v1.tcp.server;

import general.NodeRole;
import general.Printer;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTCP implements Printer {
    private static ServerSocket serverSocket;
    static ClientThread threadA, threadB, threadMC;

    static void assignThread(ClientThread thread, NodeRole role) {
        switch (role) {
            case A -> {
                threadA = thread;
                A.assignNode();
            }
            case B -> {
                threadB = thread;
                B.assignNode();
            }
            case MC -> {
                threadMC = thread;
                MC.assignNode();
            }
        }
    }

    public static int PORT = 6666;

    final static Node A, B, MC;

    static {
        A = new Node("1");
        B = new Node("2");
        MC = new Node("3");
    }

    public static boolean isRunning = true;

    static void tryToSetFinishedTaskFlag() {
        if (threadA != null && threadB != null && threadMC != null)
            if (threadA.finishedTask && threadB.finishedTask && threadMC.finishedTask) {
                threadA.finishedTask = threadB.finishedTask = threadMC.finishedTask = false;
            }
    }

    public static void start() {
        try {

            serverSocket = new ServerSocket(PORT);
            print("SERVER STARTED. Waiting on port: " + PORT);
            do {
                new Thread(new ClientThread(serverSocket.accept())).start();
            } while (isRunning && (!A.isAssigned() || !B.isAssigned() || !MC.isAssigned()));

            print("All clients connected successfully. New clients will be mocked.");
            while (isRunning) {
                new Thread(new MockClient(serverSocket.accept())).start();
            }
            stop();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    static String getPassword(NodeRole role) {
        switch (role) {
            case A -> {
                return A.getPassword();
            }
            case B -> {
                return B.getPassword();
            }
            case MC -> {
                return MC.getPassword();
            }
        }
        return null;
    }

    static void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerTCP.start();
    }

    public static void print(String message) {
        System.out.println(Printer.ANSI_GREEN + message);
    }
}
