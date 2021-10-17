package v2.algorithms;

public interface EncryptionAlgorithm {
    String encrypt(String txt,String key);
    String decrypt(String txt,String key);
}
