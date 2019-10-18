package com.gogoyang.yaofan.utility;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GogoTools {
    /**
     * 生成一个UUID
     *
     * @return
     */
    public static UUID UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    /**
     * 生成一个随机字符串
     * @param length
     * @return
     * @throws Exception
     */
    public static String generateString(int length) throws Exception{
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 对用户密码进行MD5加密
     *
     * @param password
     * @return
     * @throws Exception
     */
    public static String encoderByMd5(String password) throws Exception {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newpass = base64en.encode(md5.digest(password.getBytes("utf-8")));
        return newpass;
    }

    /**
     * SHA256 加密
     *
     * @param password
     * @return
     * @throws Exception
     */
    public static String encoderBySHA256(String password) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes("UTF-8"));
        password = Base64.encodeBase64String(messageDigest.digest());
        return password;
    }

    /**
     * 生成一个AES秘钥
     *
     * @return
     * @throws Exception
     */
    public static String generateAESKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * AES加密
     *
     * @param codec
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptAESKey(String codec, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        byte[] dataBytes = codec.getBytes("UTF-8");//如果有中文，记得加密前的字符集
        SecretKey keyspec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec);
        byte[] encrypted = cipher.doFinal(dataBytes);
        String outCode = Base64.encodeBase64String(encrypted);
        return outCode;
    }

    /**
     * 用AES解密
     *
     * @param codec
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptAESKey(String codec, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
        byte[] keyByte = Base64.decodeBase64(key);
        key = "hctrsZ7+ZHFJoR5iWChnQA==";
        Key AESKEY = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        cipher.init(Cipher.DECRYPT_MODE, AESKEY);
        byte[] result = cipher.doFinal(Base64.decodeBase64(codec));
        String src = new String(result);
        return src;
    }

    /**
     * 生成一对RSA公钥和私钥
     *
     * @return
     * @throws Exception
     */
    public static Map generateRSAKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map out = new HashMap();
        out.put("publicKey", Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        out.put("privateKey", Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        return out;
    }

    /**
     * 用私钥来解密
     *
     * @param src
     * @param rsaPrivateKey
     * @return
     * @throws Exception
     */
    public static String decryptRSAByPrivateKey(String src, String rsaPrivateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(rsaPrivateKey.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(src.getBytes()));
        Map out = new HashMap();
        return new String(result);
    }

    public static Map decryptRSA(String src, RSAPublicKey rsaPublicKey) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(src.getBytes());
        Map out = new HashMap();
        out.put("result", result);
        return out;
    }

    public static Date formatStrUTCToDateStr(String utcTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
//        TimeZone utcZone = TimeZone.getTimeZone("GMT+00:00");
//        TimeZone utcZone = TimeZone.getDefault();
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        sf.setTimeZone(utcZone);
        Date date = null;
        String dateTime = "";
        try {
            date = sf.parse(utcTime);
            dateTime = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int compare_date(String DATE1, String DATE2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else {
                if (dt1.getTime() < dt2.getTime()) {
                    System.out.println("dt1在dt2后");
                    return -1;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static boolean compare_date(Date DATE1, Date DATE2) throws Exception {
        Date dt1 = DATE1;
        Date dt2 = DATE2;
        if (dt1.getTime() > dt2.getTime()) {
            return true;
        }
        return false;
    }
}
