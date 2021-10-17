package v2;

import v2.algorithms.EncryptionAlgorithmAES;
import v2.messenger.Messenger;
import v2.nodes.A;
import v2.nodes.B;
import v2.nodes.MC;

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
