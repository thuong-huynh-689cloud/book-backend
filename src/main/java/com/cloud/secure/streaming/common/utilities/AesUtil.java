/*
 * Copyright (c) 689Cloud LLC. All Rights Reserved.
 * This software is the confidential and proprietary information of 689Cloud,
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with 689Cloud.
 */
package com.cloud.secure.streaming.common.utilities;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Quy Duong
 */
public class AesUtil {

    public final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String AES_PUBLIC_KEY = "f9c48f382012cac69ffcc78da220123f";
    private static AesUtil instance;
    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;
    private static final String CHARSET_ENCODING = "UTF-8";

    private AesUtil(int keySize, int iterationCount) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public static AesUtil getInstance() {
        try {
            synchronized (AesUtil.class) {
                if (instance == null) {
                    // set JCE for Java 8 Update 151 and higher
                    Security.setProperty("crypto.policy", "unlimited");
                    instance = new AesUtil(256, 1000);
                }
            }
            return instance;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.INFO, null, e);
            return getInstance();
//            return null;
        }
    }

    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }

    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String decryptAES(String message) {
        return getInstance().decrypt(AES_PUBLIC_KEY, AES_PUBLIC_KEY, AES_PUBLIC_KEY, message);
    }

    public static String ecryptAES(String message) {
        return getInstance().encrypt(AES_PUBLIC_KEY, AES_PUBLIC_KEY, AES_PUBLIC_KEY, message);
    }

    public String encrypt(String salt, String iv, String passphrase, String plaintext) {

        String message = null;
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            message = Base64.encodeBase64String(encrypted);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encrypt AES Error", e);
        }

        return message;
    }

    public String decrypt(String salt, String iv, String passphrase, String cipherText) {

        String message = null;
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, Base64.decodeBase64(cipherText.getBytes()));
            message = new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Decrypt AES Error", e);
        }
        return message;
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {

        byte[] result = null;
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            result = cipher.doFinal(bytes);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error("AesUtil.doFinal()", e);
        }
        return result;
    }

    private SecretKey generateKey(String salt, String passphrase) {

        SecretKey key = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("AesUtil.generateKey()", e);
        }

        return key;
    }

    /**
     * decrypt the data
     *
     * @param strToDecrypt encrypted string
     * @return plain text
     */
    public static String decryptData(@NotNull String key, String strToDecrypt) {
        try {
            String ENCRYPT_ECB_ALGORITHM = "AES/ECB/PKCS5Padding";
            Cipher cipher = Cipher.getInstance(ENCRYPT_ECB_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private static SecretKeySpec getKey(String inputKey) {
        try {
            /*
             *Do md5 to token for convert token to 16 digit
             */
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] key = inputKey.getBytes(CHARSET_ENCODING);
            key = md.digest(key);

            // get 16 byte value from token
            String d = new BigInteger(1, key).toString(16);

            // generate secretKey using token
            String AES_ALGORITHM = "AES";
            return new SecretKeySpec(d.substring(0, 16).getBytes(CHARSET_ENCODING), AES_ALGORITHM);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
