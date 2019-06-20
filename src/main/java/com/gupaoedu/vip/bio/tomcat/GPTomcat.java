package com.gupaoedu.vip.bio.tomcat;

import com.gupaoedu.vip.bio.tomcat.http.GPRequest;
import com.gupaoedu.vip.bio.tomcat.http.GPResponse;
import com.gupaoedu.vip.bio.tomcat.http.GPServlet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GPTomcat {

    private int port = 8080;
    private ServerSocket server;

    private Map<String, GPServlet> servletMap = new HashMap<>();
    private Properties webXml = new Properties();

//    J2EE标准
//    Servlet Request Response

//    思路
//    1、配置好端口，默认8080 ，serverSocket  IP："localhost"
//    2、配置web.xml文件   ,自己写的Servlet 需要继承自HttpServlet才能被识别
//          servlet-name
//          servlet-class
//          url-patten
//    3、读取配置文件，url-patten 和 Servlet 建立一个映射关系
//          Map   servletMap
//    4、HTTP请求，发送的数据就是字符串，有规律的字符串（符合HTTP协议）
//    5、从协议内容中拿到URL，把相应的servlet用反射进行实例化
//    6、调用实例化对象的service()方法，执行具体的逻辑，doGet(),doPost()方法
//    7、Request（InputStream），Response(outputStream)

    /**
     * 1、加载配置文件/2/3、同时初始化servletMapping对象
     */
    private void init(){

        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");

            webXml.load(fis);

            for (Object k:webXml.keySet()){
                String key = k.toString();

                if(key.endsWith(".url")){
                    String servletName = key.replaceAll("\\.url$","");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName+".className");

//                    serlvet 单实例，多线程
                    GPServlet servlet = (GPServlet) Class.forName(className).newInstance();
                    servletMap.put(url,servlet);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 服务端启动
     */
    public void start(){
        init();

        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("GP Tomcat服务端口已启动，监听端口为："+this.port);

//            用死循环，等待客户端请求。
            while (true){
                Socket socket = server.accept();
                
                process(socket);
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 4、处理用户请求
     * @param client
     */
    private void process(Socket client) throws Exception{
        InputStream is = client.getInputStream();
        OutputStream os = client.getOutputStream();

        GPRequest request = new GPRequest(is);
        GPResponse response = new GPResponse(os);

        String url = request.getUrl();

        if(servletMap.containsKey(url)){
            servletMap.get(url).service(request,response);
        }else {
            response.write("404 - Servlet Not Found");
        }

        os.flush();
        os.close();
        is.close();
        client.close();



    }


    public static void main(String[] args) {
        new GPTomcat().start();
    }

}
