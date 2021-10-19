package general.algorithms;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ECBAlgorithm implements EncryptionAlgorithmAES {
    @Override
    public List<String> encrypt(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            //divide string into blocks of fixed size (128) for ECB
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_ECB);

            for (var block : blocksList) {
                var encryptedBlock = encryptBlock(block, key);  //encrypt each block with a key
                cipherTextList.add(convertByteToString(encryptedBlock));    //add it to the list
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    @Override
    public String decrypt(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (var encryptedBlockAsString : cipherTextList) {
                var encryptedBlock = convertStringToByte(encryptedBlockAsString);

                var decryptedBlock = decryptBlock(encryptedBlock, key);
                decryptedBlock = removePaddingIfNecessary(decryptedBlock, BlockSizeAES.BLOCK_SIZE_ECB);

                stringBuilder.append(convertByteToString(decryptedBlock));
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    private byte[] encryptBlock(byte[] block, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        return c.doFinal(block);
    }

    private byte[] decryptBlock(byte[] block, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        return c.doFinal(block);
    }

}
