package v2.algorithms;

public class XXXAlgorithm implements EncryptionAlgorithm {
    @Override
    public String encrypt(String txt, String key) {
        return txt;
    }

    @Override
    public String decrypt(String txt, String key) {
        return "decrypted " + txt + " with key:" + key;
    }
}
