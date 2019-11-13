package com.github.gxhunter.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hunter
 */
public class Md5Util {
    /**
     * 将明文转为MD5密文
     *
     * @return 加密后的32位MD5密文
     */
    public static String encode(String psd) {
        String result = null;
        try {
            //1,指定加密算法类型
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2,将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
            byte[] bs = digest.digest(psd.getBytes());
            //3,循环遍历bs,然后让其生成32位字符串,固定写法
            //4,拼接字符串过程
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b : bs) {
                int i = b & 0xff;
                //int类型的i需要转换成16机制字符
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            result = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
