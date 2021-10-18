package general.algorithms;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.List;

public class ECBAlgorithm implements EncryptionAlgorithmAES {
    @Override
    public List<String> encrypt(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = List.of(plainText);
        return cipherTextList;
    }

    @Override
    public String decrypt(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        return cipherTextList.get(0);
    }


//    @Override
//    public String customEncrypt(String inputData, SecretKey key, IvParameterSpec iv) {
//        String msg = null;
//        try {
//            msg = ECBAlgorithm.encrypt(inputData, key, iv);
//        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }
//
//    @Override
//    public String customDecrypt(String inputData, SecretKey key, IvParameterSpec iv) {
//        String msg = null;
//        try {
//            msg = ECBAlgorithm.decrypt(inputData, key, iv);
//        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }
//
//
//    public static String encrypt(String input, SecretKey key, IvParameterSpec iv)
//            throws NoSuchPaddingException, NoSuchAlgorithmException,
//            InvalidAlgorithmParameterException, InvalidKeyException,
//            BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] cipherText = cipher.doFinal(input.getBytes());
//        return Base64.getEncoder().encodeToString(cipherText);
//
//    }
//
//    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv)
//            throws NoSuchPaddingException, NoSuchAlgorithmException,
//            InvalidAlgorithmParameterException, InvalidKeyException,
//            BadPaddingException, IllegalBlockSizeException {
//
//        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
//        return new String(plainText);
//    }
}
