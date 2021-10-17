package v2.algorithms;

public class ECBAlgorithm implements EncryptionAlgorithm {
    @Override
    public String encrypt(String txt, String key) {
        return "encrypted_" + txt;
    }

    @Override
    public String decrypt(String txt, String key) {
        txt = txt.replace("encrypted_", "");
        return "decrypted_" + txt;
    }
}
