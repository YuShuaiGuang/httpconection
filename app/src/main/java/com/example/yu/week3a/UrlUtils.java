package com.example.yu.week3a;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by YU on 2017/6/27.
 */
public class UrlUtils {
    //get请求
    public static String getData(String path){
        try {
            URL url=new URL(path);
            //通过url的openConnection方法得到httpurlconnection
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            if (connection.getResponseCode()==200){
                InputStream is=connection.getInputStream();
                byte[]buff=new byte[1024];
                int len=-1;
                StringBuffer sb=new StringBuffer();
                while ((len=is.read(buff))!=-1){
                    sb.append(new String(buff,0,len,"utf-8"));
                }
                is.close();
                connection.disconnect();//关闭连接
                return  sb.toString();
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
