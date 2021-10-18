package v1.tcp.server;

import general.NodeRole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;

    private NodeRole role;
    boolean finishedTask = false;

    public ClientThread(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            getRole();
            System.out.println("This is: " + ANSI_YELLOW + role + ANSI_RESET + " waiting for requests to send or get messages.");
            awaitRequest();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    private void getRole() throws IOException {
        try {
            do {
                String clientMessage = in.readLine().toUpperCase();

                NodeRole tempRole = switch (clientMessage) {
                    case "A" -> NodeRole.A;
                    case "B" -> NodeRole.B;
                    case "MC" -> NodeRole.MC;
                    default -> null;
                };

                if (tempRole == null) {
                    out.println("Invalid role. Please input one of the fallowing: A B or MC.    " + clientMessage);
                    continue;
                }

                if (attemptLogin(tempRole)) {
                    role = tempRole;
                    ServerTCP.assignThread(this, role);
                    out.println("Connected successfully.");
                    this.finishedTask = true;
                    break;
                }
            } while (true);
            ServerTCP.tryToSetFinishedTaskFlag();

        } catch (IOException e) {
            e.printStackTrace();
            ServerTCP.stop();
        }
    }


    private void awaitRequest() {

        while (true) {
            try {
                while (this.finishedTask)
                    Thread.sleep(1000);         //wait for other threads to finish their tasks

                out.println("[START TASK]");           //signal nodes to start actions

                readRequestsTillFlagIsReceived();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readRequestsTillFlagIsReceived() {
        try {
            String requestString;
            do {
                requestString = in.readLine();

                if (requestString == null) {
                    System.err.println("READING EMPTY LINE");
                    break;
                }

                if (requestString.startsWith("[F]")) break;

                switch (getTypeOfMessage(requestString)) {
                    case SEND -> sendMessage(requestString);
                    case READ -> readMessage();
                    case SIGNAL -> {    //do nothing
                    }
                }

            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.finishedTask = true;
        ServerTCP.tryToSetFinishedTaskFlag();
    }


    private void sendMessage(String message) {
        var toWho = headerToNodeRoleParser(getMessageHead(message));

        if (toWho != null)
            sendMessageParser(toWho, getMessageWithoutHead(message));

    }

    private NodeRole headerToNodeRoleParser(String messageHead) {
        switch (messageHead) {
            case "A" -> {
                return NodeRole.A;
            }
            case "B" -> {
                return NodeRole.B;
            }
            case "MC" -> {
                return NodeRole.MC;
            }
        }
        return null;
    }

    private TypeOfMessage getTypeOfMessage(String message) {
        switch (getMessageHead(message)) {
            case "R" -> {                       //Read messages flag
                return TypeOfMessage.READ;
            }
            case "N" -> {                       //No prefix message
                return TypeOfMessage.SIGNAL;
            }
            default -> {
                return TypeOfMessage.SEND;
            }
        }
    }

    private String getMessageHead(String message) {
        if (message.length() <= 2) return "N";
        var index1 = message.indexOf('[');
        var index2 = message.indexOf(']');
        return message.substring(index1 + 1, index2);
    }

    private String getMessageWithoutHead(String message) {
        var index3 = message.indexOf(']');
        return message.substring(index3 + 1);
    }

    private void sendMessageParser(NodeRole toWho, String message) {
        switch (toWho) {
            case A -> ServerTCP.A.sendMessage(message);
            case B -> ServerTCP.B.sendMessage(message);
            case MC -> ServerTCP.MC.sendMessage(message);
        }
    }

    private boolean attemptLogin(NodeRole role) {
        switch (role) {
            case A -> {
                if (ServerTCP.A.isAssigned()) {
                    out.println("A is already assigned.");
                    return false;
                }
            }
            case B -> {
                if (ServerTCP.B.isAssigned()) {
                    out.println("B is already assigned.");
                    return false;
                }
            }
            case MC -> {
                if (ServerTCP.MC.isAssigned()) {
                    out.println("MC is already assigned.");
                    return false;
                }
            }
        }
        return true;
//        String password;
//        password = askForPassword();
//        return Objects.equals(ServerTCP.getPassword(role), password);

    }

    private String askForPassword() {
        return "";
    }


    private void readMessage() {
        switch (role) {
            case A -> callGetMessage(ServerTCP.A);
            case B -> callGetMessage(ServerTCP.B);
            case MC -> callGetMessage(ServerTCP.MC);
        }
    }

    private void callGetMessage(Node node) {
        String msg = node.getMessage();
        while (msg != null) {
            out.println(msg);
            msg = node.getMessage();
        }
        out.println("[D]"); //DONE
    }
}
