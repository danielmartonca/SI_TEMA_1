package general.algorithms;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public interface EncryptionAlgorithmAES {

    //methods for keys IV and so on
    static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    static String convertSecretKeyToString(SecretKey secretKey) {
        byte[] rawData = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(rawData);
    }

    static SecretKey convertStringToSecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }


    //default methods used by the encryption algorithms

    default String convertByteToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    default byte[] convertStringToByte(String string) {
        return string.getBytes(StandardCharsets.ISO_8859_1);
    }


    default byte[] addPaddingIfNecessary(byte[] encryptedBlock, BlockSizeAES size) {

        byte[] blockBytes = new byte[size.getSize()];

        //add remaining bytes padded with 0 to be multiple of size.getSize()
        if (size.getSize() > encryptedBlock.length) {
            System.arraycopy(encryptedBlock, 0, blockBytes, 0, encryptedBlock.length);
            return blockBytes;
        }

        return encryptedBlock;
    }

    default byte[] getBytesArraysOfMultipleLength(byte[] bytesArray, BlockSizeAES size) {
        List<byte[]> list = new LinkedList<>();

        int i = 0;
        while (i + size.getSize() <= bytesArray.length) {
            byte[] blockBytes = new byte[size.getSize()];
            System.arraycopy(bytesArray, i, blockBytes, 0, size.getSize());//create list of byte arrays of length: size.getSize()
            list.add(blockBytes);

            i += size.getSize();
        }

        //add remaining bytes padded with 0 to be multiple of size.getSize()
        if (i + size.getSize() > bytesArray.length) {
            byte[] blockBytes = new byte[size.getSize()];
            System.arraycopy(bytesArray, i, blockBytes, 0, bytesArray.length - i);

            i += size.getSize();
            list.add(blockBytes);
        }


        byte[] totalBytes = new byte[i];
        i = 0;
        for (var byteSequence : list) {
            System.arraycopy(byteSequence, 0, totalBytes, i, size.getSize());
            i += size.getSize();
        }

        return totalBytes;
    }


    default byte[] removePaddingIfNecessary(byte[] encryptedBlock, BlockSizeAES size) {
        int i;
        for (i = encryptedBlock.length - 1; i >= 0; i--)
            if (encryptedBlock[i] != 0)
                break;
        byte[] encryptedBlockNoPadding = new byte[i + 1];
        System.arraycopy(encryptedBlock, 0, encryptedBlockNoPadding, 0, i + 1);
        return encryptedBlockNoPadding;
    }

    default List<byte[]> getStringAsByteBlocksList(String plainText, BlockSizeAES size) {
        var plainTextBytes = plainText.getBytes();
        List<byte[]> list = new LinkedList<>();

        int i = 0;
        while (i + size.getSize() <= plainTextBytes.length) {
            byte[] blockBytes = new byte[size.getSize()];
            System.arraycopy(plainTextBytes, i, blockBytes, 0, size.getSize());//create list of byte arrays of length: size.getSize()
            list.add(blockBytes);

            i += size.getSize();
        }

        //add remaining bytes padded with 0 to be multiple of size.getSize()
        if (i + size.getSize() > plainTextBytes.length) {
            byte[] blockBytes = new byte[size.getSize()];
            System.arraycopy(plainTextBytes, i, blockBytes, 0, plainTextBytes.length);
            list.add(blockBytes);
        }
        return list;
    }


    //abstract methods
    List<String> encrypt(String plainText, SecretKey key, IvParameterSpec iv);

    String decrypt(List<String> cipherTextList, SecretKey key, IvParameterSpec iv);
}
