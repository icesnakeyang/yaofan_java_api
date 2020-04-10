package com.gogoyang.yaofan.controller.wx.util;


import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;


/**
 * Created by sxp on 2019/12/13.
 * <p>
 * AES-128-CBC 加密方式
 * 注：
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 */

public class AesCbcUtil {

    private static MessageDigest sMd5MessageDigest;
    private static StringBuilder sStringBuilder;
    static {
        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
        sStringBuilder = new StringBuilder();

        try {
            sMd5MessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {

        }

    }

    /**
     * AES解密
     *
     * @param data           //密文，被加密的数据
     * @param key            //秘钥
     * @param iv             //偏移量
     * @param encodingFormat //解密后的结果需要进行的编码
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key, String iv, String encodingFormat) throws Exception, UnsupportedEncodingException {
//        initialize();

        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);


        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));

            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, encodingFormat);
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String md5(String s) {
        sMd5MessageDigest.reset();
        sMd5MessageDigest.update(s.getBytes());
        byte[] digest = sMd5MessageDigest.digest();
        sStringBuilder.setLength(0);

        for (int i = 0; i < digest.length; ++i) {
            int b = digest[i] & 255;
            if (b < 16) {
                sStringBuilder.append('0');
            }

            sStringBuilder.append(Integer.toHexString(b));
        }

        return sStringBuilder.toString().toUpperCase();
    }


    public static String getRefundDecrypt(String reqInfoSecret, String key) {
        String result = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bt = decoder.decodeBuffer(reqInfoSecret);
            String b = new String(bt);
            String md5key = md5(key).toLowerCase();
            SecretKey secretKey = new SecretKeySpec(md5key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] resultbt = cipher.doFinal(bt);
            result = new String(resultbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static void main(String[] args) {
//        String A = "cbRfjwOKrLQCOjAU0v1Ac6cDJQhK6349ngVplZHLe010obV59BJ75z05eUzG+Um3Zk9WxNO6jtk60bBDkMLmf4dZk41wSbyx/GiNNcRp3g0GdUMjGjhlyJgMAKiLbmVwcPEQnOoikcCbwGd0VmOdWcTk67kDlE4ssYW6pgBXp5bSBVotuar2Wxi0z20HGgsz7dcIVELP9+JfGwuiVV9xkhO8sPbEO4SIO2qhkRQ0QzbDYgn9gU1Iprzv6wGxFh+Bm/lJWuiBGwhFCT2fq1xEF1nDPEyW3LOWq4daCegXpvTXoXLZp9Xp6zcdxiDCsLgj8yj+q7ZNmVNt1vTUR94ZQAI1UAtCqK+1dI89DZdbifU7o0fVm/9WQqjcOIo4WemUJU7WfCsyHXBVvx8lZezoQc5ZGZOqTEZJCVKV6OF5iqvlNnZJ1byPg7BfHZnmbIjdETFMIOkq3oPicshnNlVZ9g08DvesDZzG/KJzI8NBMFfXNMjSJyuemsZ/0jFJRtKDOWoCQetWox+mORa5BPrMwvibTLkPZL0okvnyrJgFwGtE4BFTdnN/+cfuUISXMTbeZv6UjRJwD9y8B+wi4wTSUC1QXjlZEkV+RgBNnJD/n13NWUK1nlrwev/RHLsgjoMSYnF0mYec8g1BCyW0POcp4iEDaRVMrjQnACyyeMncSxA+KawdoFEOqMuAITv3B0Q9iZOTpI7yqnpquWiphXMuKovJgLp4vnPqerMMthTeaQ/rfVVO/U6Z4K/heTZKT2Y0y7kQP2GS+r/N5Qvu+J0sihJ8opoZ0AJ7ktwGrzcmkIq/DCCtAfgdAT8x3rtSXA4f8pb7WqFssdGRqgzMZR1jlcz3LD5+Du+BWK5QmyCeynywd9b6s7oYdyU0aI75LrT5EBOtEHAMGi2rmwb1X2wlEd3g4gCr2l1EEA4dUTdhsqynG0w64VHWJkvJgTOdmzMPWYksLu463qcy6dN44WuNBG0aw7uEQQIUXeRhDEwCBQVmzXNLzwQ6wHv6O9j7yvkvZll3SuGw7bbsfdSdAfQkJkLCj/MHuEKUauiV0mQS/2M=";
//        String key = "02a86cb980b1237eaa77fb86adf64657";
//        String B = getRefundDecrypt(A, key);
//        System.out.println(B);
//    }

}
