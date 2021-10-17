package v2.algorithms;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class XXXAlgorithm implements EncryptionAlgorithmAES {
    @Override
    public String customEncrypt(String inputData, SecretKey key, IvParameterSpec iv) {
        return inputData;
    }

    @Override
    public String customDecrypt(String inputData, SecretKey key, IvParameterSpec iv) {
        return inputData;
    }
}
