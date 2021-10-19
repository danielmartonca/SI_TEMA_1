import general.algorithms.ECBAlgorithm;
import general.algorithms.EncryptionAlgorithmAES;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class EncryptionTest {

    EncryptionAlgorithmAES algorithm = new ECBAlgorithm();

    final String shortText = "abcd";
    final String longText = "123192hjkansfdo812i31j23y891dbad98fu123u1h23812";

    @Test
    public void ECBTest() throws NoSuchAlgorithmException {
//        var key = EncryptionAlgorithmAES.generateKey(128);
//        var iv = EncryptionAlgorithmAES.generateIv();
//        var msgList = algorithm.encrypt(shortText, key, iv);
//
//        algorithm.decrypt(msgList, key, iv);
    }
}
