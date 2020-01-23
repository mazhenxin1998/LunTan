package com.example.myboot07.config;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  MD5加密算法
 */
public class MD5Encription {


    public static String digest(String str){
        return  digest32(str,32);
    }


    public static String digest32(String str,int rang){

        MessageDigest md5 = null ;

        if(str.equals("")){
            return "";
        }

        try {
            // 初始化MD5
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        // 将字符串转换为字节数组并进行摘要
        char[] chars = str.toCharArray();
        byte[] b = new byte[chars.length];

        //  转换
        for(int i=0;i<b.length;i++){
            b[i] = (byte) chars[i];
        }

        //  对字节数组进行摘要  摘要之后 仍然是字节数组

        byte[] bytes = md5.digest(b);

        // 对于摘要后的字节数组，对于每一个字节不足16位的自动补足16位 并将结果保存在一个StringBuffer中
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<bytes.length;i++){

            // 将每一个字节转换为16位
            int val = ((int)bytes[i]) & 0xff;

            if(val <=16){
                sb.append("0");
            }else{
                sb.append(Integer.toString(val));
            }

        }

        if(rang == 32){
            return sb.toString();
        }else{
            return sb.toString().substring(8,24);
        }


    }

}
