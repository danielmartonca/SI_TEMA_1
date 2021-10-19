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

    private byte[] xor(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length)
            return null;
        for (int i = 0; i < bytes1.length; i++)
            bytes1[i] = (byte) (bytes1[i] ^ bytes2[i]);
        return bytes1;
    }

    //CBC
    public List<String> encryptCBC(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            //divide string into blocks of fixed size (128) for ECB
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_ECB);

            //do one iteration
            var xorBlock = blocksList.get(0);
            var encryptedBlock = encryptBlockCBC(xor(xorBlock, iv.getIV()), key, iv);  //encrypt each block with a key
            cipherTextList.add(convertByteToString(encryptedBlock));
            xorBlock = encryptedBlock;

            //repeat until it's finished
            for (int i = 1; i < blocksList.size(); i++) {
                var block = blocksList.get(i);
                block = xor(block, xorBlock);
                encryptedBlock = encryptBlockCBC(block, key, iv);  //encrypt each block with a key
                cipherTextList.add(convertByteToString(encryptedBlock));
                xorBlock = encryptedBlock;
            }

        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    public String decryptCBC(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //one iteration
            var encryptedBlockAsString = cipherTextList.get(0);
            var encryptedBlock = convertStringToByte(encryptedBlockAsString);
            var xorBlock = encryptedBlock;
            var decryptedBlock = decryptBlockCBC(encryptedBlock, key, iv);
            //decrypted block
            stringBuilder.append(convertByteToString(xor(decryptedBlock, iv.getIV())));

            for (int i = 1; i < cipherTextList.size(); i++) {
                encryptedBlockAsString = cipherTextList.get(i);
                encryptedBlock = convertStringToByte(encryptedBlockAsString);
                decryptedBlock = decryptBlockCBC(encryptedBlock, key, iv);
                //decrypted block
                var value = xor(decryptedBlock, xorBlock);
                stringBuilder.append(convertByteToString(value));
                xorBlock = encryptedBlock;
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

    //OFB
    public List<String> encryptOFB(String plainText, SecretKey key, IvParameterSpec iv) {
        List<String> cipherTextList = new LinkedList<>();
        try {
            var blocksList = getStringAsByteBlocksList(plainText, BlockSizeAES.BLOCK_SIZE_OFB);

            var xorBlock = iv.getIV();
            var encryptionValue = encryptBlockOFB(xorBlock, key, iv);
            var xorValue = encryptionValue;

            var plainTextAsByte = convertStringToByte(convertByteToString(blocksList.get(0)));
            var value = xor(encryptionValue, plainTextAsByte);
            cipherTextList.add(convertByteToString(value));

            for (int i = 1; i < blocksList.size(); i++) {
                encryptionValue = encryptBlockOFB(xorValue, key, iv);
                xorValue = encryptionValue;

                plainTextAsByte = convertStringToByte(convertByteToString(blocksList.get(i)));
                value = xor(encryptionValue, plainTextAsByte);
                cipherTextList.add(convertByteToString(value));
            }

        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherTextList;
    }

    public String decryptOFB(List<String> cipherTextList, SecretKey key, IvParameterSpec iv) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            var ivByteArray = iv.getIV();
            var decryptedBlock = decryptBlockOFB(ivByteArray, key, iv);

            var xorValue = decryptedBlock;
            stringBuilder.append(convertByteToString(xor(decryptedBlock, convertStringToByte(cipherTextList.get(0)))));

            for (int i = 1; i < cipherTextList.size(); i++) {
                decryptedBlock = decryptBlockOFB(xorValue, key, iv);
                xorValue = decryptedBlock;

                stringBuilder.append(convertByteToString(xor(decryptedBlock, convertStringToByte(cipherTextList.get(i)))));
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


}
