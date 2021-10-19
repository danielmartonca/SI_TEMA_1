import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import general.algorithms.XXXAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class EncryptionTest {


    final String shortText = "abcd";
    final String longText = "123192hjkansfdo812i31j23y891dbad98fu123u1h23812";

    @Test
    public void ECBTest() throws NoSuchAlgorithmException {
        var key = EncryptionAlgorithmAES.generateKey(128);
        var iv = EncryptionAlgorithmAES.generateIv();
        EncryptionAlgorithmAES algorithm = new ECBAlgorithm();

        var msgList = algorithm.encrypt(shortText, key, iv);

        var decryptedValue = algorithm.decrypt(msgList, key, iv);
        System.out.println(decryptedValue);
    }

    @Test
    public void XXXTest() throws NoSuchAlgorithmException {
        var key = EncryptionAlgorithmAES.generateKey(128);
        var iv = EncryptionAlgorithmAES.generateIv();

        System.out.println(shortText);
        EncryptionAlgorithmAES algorithm = new XXXAlgorithm();
        var msgList = algorithm.encrypt(shortText, key, iv);

        System.out.println(msgList);

        var decryptedValue = algorithm.decrypt(msgList, key, iv);
        System.out.println(decryptedValue);
    }
}
