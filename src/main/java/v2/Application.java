package v2;

import v2.messenger.Messenger;
import v2.nodes.A;
import v2.nodes.B;
import v2.nodes.MC;

public class Application {
    public static void main(String[] args) {
        var messenger = new Messenger();
        new Thread(new A(messenger)).start();
        new Thread(new B(messenger)).start();
        new Thread(new MC(messenger)).start();
    }
}
