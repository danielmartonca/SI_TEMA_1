package general.algorithms;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class XXXAlgorithm implements EncryptionAlgorithmAES {
    private static final AlgorithmXXX algorithm = AlgorithmXXX.CBC;

    @Override
    public List<String> encrypt(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> list = null;
        switch (algorithm) {
            case CBC -> list = encryptCBC(plainText, key, iv);
            case OFB -> list = encryptOFB(plainText, key, iv);
            case CFB -> list = encryptCFB(plainText, key, iv);
        }
        return list;
    }

    @Override
    public String decrypt(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        String s = null;
        switch (algorithm) {
            case CBC -> s = decryptCBC(cipherTextList, key, iv);
            case OFB -> s = decryptOFB(cipherTextList, key, iv);
            case CFB -> s = decryptCFB(cipherTextList, key, iv);
        }
        return s;
    }


    //CBC
    public List<String> encryptCBC(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            //divide string into blocks of fixed size (128) for ECB
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_ECB);

            for (var block : blocksList) {
                var encryptedBlock = encryptBlockCBC(block, key, iv);  //encrypt each block with a key
                cipherTextList.add(convertByteToString(encryptedBlock));    //add it to the list
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    public String decryptCBC(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (var encryptedBlockAsString : cipherTextList) {
                var encryptedBlock = convertStringToByte(encryptedBlockAsString);

                var decryptedBlock = decryptBlockCBC(encryptedBlock, key, iv);
                decryptedBlock = removePaddingIfNecessary(decryptedBlock, BlockSizeAES.BLOCK_SIZE_ECB);

                stringBuilder.append(convertByteToString(decryptedBlock));
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private byte[] encryptBlockCBC(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, key, iv);
        return c.doFinal(block);
    }

    private byte[] decryptBlockCBC(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
        c.init(Cipher.DECRYPT_MODE, key, iv);
        return c.doFinal(block);
    }


    //CFB
    public List<String> encryptCFB(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            //divide string into blocks of fixed size (128) for ECB
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_CFB);

            for (var block : blocksList) {
                var encryptedBlock = encryptBlockCFB(block, key, iv);  //encrypt each block with a key
                cipherTextList.add(convertByteToString(encryptedBlock));    //add it to the list
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    public String decryptCFB(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (var encryptedBlockAsString : cipherTextList) {
                var encryptedBlock = convertStringToByte(encryptedBlockAsString);

                var decryptedBlock = decryptBlockCFB(encryptedBlock, key, iv);
                decryptedBlock = removePaddingIfNecessary(decryptedBlock, BlockSizeAES.BLOCK_SIZE_CFB);

                stringBuilder.append(convertByteToString(decryptedBlock));
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private byte[] encryptBlockCFB(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/CFB8/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, key, iv);
        return c.doFinal(block);
    }

    private byte[] decryptBlockCFB(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/CFB8/NoPadding");
        c.init(Cipher.DECRYPT_MODE, key, iv);
        return c.doFinal(block);
    }


    //OFB
    public List<String> encryptOFB(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            //divide string into blocks of fixed size (128) for ECB
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_OFB);

            for (var block : blocksList) {
                var encryptedBlock = encryptBlockOFB(block, key, iv);  //encrypt each block with a key
                cipherTextList.add(convertByteToString(encryptedBlock));    //add it to the list
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    public String decryptOFB(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (var encryptedBlockAsString : cipherTextList) {
                var encryptedBlock = convertStringToByte(encryptedBlockAsString);

                var decryptedBlock = decryptBlockOFB(encryptedBlock, key, iv);
                decryptedBlock = removePaddingIfNecessary(decryptedBlock, BlockSizeAES.BLOCK_SIZE_OFB);

                stringBuilder.append(convertByteToString(decryptedBlock));
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private byte[] encryptBlockOFB(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/OFB32/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, iv);
        return c.doFinal(block);
    }

    private byte[] decryptBlockOFB(byte[] block, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher c = Cipher.getInstance("AES/OFB32/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, iv);
        return c.doFinal(block);
    }

}
