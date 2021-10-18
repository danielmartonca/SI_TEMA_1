package v2.threads;

import general.algorithms.EncryptionAlgorithmAES;
import v2.threads.messenger.Messenger;
import v2.threads.nodes.A;
import v2.threads.nodes.B;
import v2.threads.nodes.MC;

import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        var messenger = new Messenger();
        var iv = EncryptionAlgorithmAES.generateIv();
        new Thread(new A(messenger, iv)).start();
        new Thread(new B(messenger, iv)).start();
        new Thread(new MC(messenger, iv)).start();
    }

}
