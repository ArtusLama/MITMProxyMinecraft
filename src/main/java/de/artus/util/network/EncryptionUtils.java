package de.artus.util.network;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static KeyFactory RSA_KEY_FACTORY;
    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static KeyPair generateRsaKeyPair(final int keySize) {
        try {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize);
            return generator.generateKeyPair();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to generate RSA keypair", e);
        }
    }

    public static PublicKey parseRsaPublicKey(byte[] keyValue) {
        try {
            return RSA_KEY_FACTORY.generatePublic(new X509EncodedKeySpec(keyValue));
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key bytes");
        }
    }


    public static byte[] decryptRsa(KeyPair keyPair, byte[] bytes) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        return cipher.doFinal(bytes);
    }


    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String encodeVerifyToken(byte[] verifyToken) {
        return Base64.getEncoder().encodeToString(verifyToken);
    }

    public static String twosComplementHexdigest(byte[] digest) {
        return new BigInteger(digest).toString(16);
    }

    public static String generateServerId(byte[] sharedSecret, PublicKey key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(sharedSecret);
            digest.update(key.getEncoded());
            return twosComplementHexdigest(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }


}
