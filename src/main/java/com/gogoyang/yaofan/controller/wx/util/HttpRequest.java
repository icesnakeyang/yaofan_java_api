package com.gogoyang.yaofan.controller.wx.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

public class HttpRequest {

        public static void main(String[] args) {
            //发送 GET 请求
            String s=HttpRequest.sendGet("http://v.qq.com/x/cover/kvehb7okfxqstmc.html?vid=e01957zem6o", "");
            System.out.println(s);

//        //发送 POST 请求
//        String sr=HttpRequest.sendPost("http://www.toutiao.com/stream/widget/local_weather/data/?city=%E4%B8%8A%E6%B5%B7", "");
//        JSONObject json = JSONObject.fromObject(sr);
//        System.out.println(json.get("data"));
        }

        /**
         * 向指定URL发送GET方法的请求
         *
         * @param url
         *            发送请求的URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return URL 所代表远程资源的响应结果
         */
        public static String sendGet(String url, String param) {
            String result = "";
            BufferedReader in = null;
            try {
                String urlNameString = url + "?" + param;
                URL realUrl = new URL(urlNameString);
                // 打开和URL之间的连接
                URLConnection connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.out.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送GET请求出现异常！" + e);
                e.printStackTrace();
            }
            // 使用finally块来关闭输入流
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return result;
        }

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url
         *            发送请求的 URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return 所代表远程资源的响应结果
         */
        public static String sendPost(String url, String param) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送 POST 请求出现异常！"+e);
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
            finally{
                try{
                    if(out!=null){
                        out.close();
                    }
                    if(in!=null){
                        in.close();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return result;
        }

    /**
     *   
     *  *  
     *  * @param requestUrl请求地址  
     *  * @param requestMethod请求方法  
     *  * @param outputStr参数  
     *  
     */
    public static String sendRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     *
     * @param requestUrl 请求地址 
     * @param outputStr 参数 
     * @param file 证书文件
     * @return
     */
    public static String sendRequest(String requestUrl, String outputStr, File certificateFile,String certificatePassword) {

        String jsonStr = "";
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream is = new FileInputStream(certificateFile);
            try {
                keyStore.load(is, certificatePassword.toCharArray());
            } finally {
                is.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(
                    keyStore,certificatePassword.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory ssLsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
            );
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssLsf).build();
            try {
                HttpPost httpPost = new HttpPost(requestUrl); // 设置响应头信息
                httpPost.addHeader("Connection", "keep-alive");
                httpPost.addHeader("Accept", "*/*");
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                httpPost.addHeader("Host", "api.mch.weixin.qq.com");
                httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
                httpPost.addHeader("Cache-Control", "max-age=0");
                httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
                httpPost.setEntity(new StringEntity(outputStr, "UTF-8"));
                CloseableHttpResponse response = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();

                    jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                    EntityUtils.consume(entity);
                    return jsonStr;
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
