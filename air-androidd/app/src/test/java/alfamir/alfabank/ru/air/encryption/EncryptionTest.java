package alfamir.alfabank.ru.air.encryption;

//import android.util.Base64;


import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionTest {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void test() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        byte[] wtf = RSAEncrypt("hey, fucker!");
        String notWtf = RSADecrypt(wtf);


        byte[] encoded = publicKey.getEncoded();
        byte[] encodedBse64 = Base64.getEncoder().encode(encoded);

        String xml = "<RSAKeyValue>\n" +
                "<Modulus>" + new String(encodedBse64) + "</Modulus>\n" +
                "<Exponent>AQAB</Exponent>\n" +
                "</RSAKeyValue>";

        System.out.println(xml);

    }

    public byte[] RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();

        RSAPublicKey key = (RSAPublicKey) kp.getPublic();
        int modLength =  key.getModulus().toByteArray().length;

        byte[] forOleg = key.getModulus().toByteArray();
//        byte[] forDearOleg = Base64.getEncoder().encode(forOleg);
//        String forSuperDearOleg = new String(forDearOleg);
        int exponentLength = key.getPublicExponent().toByteArray().length;

        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
        System.out.println("EEncrypted = " + new String(Hex.encodeHex(encryptedBytes)));
        return encryptedBytes;
    }

    public String RSADecrypt(final byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher1.doFinal(encryptedBytes);
        String decrypted = new String(decryptedBytes);
        System.out.println("DDecrypted = " + decrypted);
        return decrypted;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

//    @Test
//    public void testChipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128, new SecureRandom());
//        SecretKey secretKey = keyGenerator.generateKey();
//
//        Cipher cipher = Cipher.getInstance("AES");
//
//        String plainText = "This is supposed to be encrypted";
//        String plainKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
//
////encrypt
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
//        String encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//
////decrypt
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[]decryptedBytes = cipher.doFinal(encryptedBytes);
//        String decryptedText = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
//        System.out.println(decryptedText);
//    }

}