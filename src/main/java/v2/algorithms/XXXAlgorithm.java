package v2.algorithms;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.LinkedList;
import java.util.List;

public class XXXAlgorithm implements EncryptionAlgorithmAES {
    @Override
    public List<String> encrypt(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = List.of(plainText);
        return cipherTextList;
    }

    @Override
    public String decrypt(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        return cipherTextList.get(0);
    }
}
