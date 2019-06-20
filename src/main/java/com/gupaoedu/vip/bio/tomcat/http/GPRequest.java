package com.gupaoedu.vip.bio.tomcat.http;

import java.io.InputStream;

public class GPRequest {

    private String method;
    private String url;

    public GPRequest(InputStream in) {
        try {
            String content = "";
            byte[] bytes = new byte[1024];
            int len = 0;
            if(((len = in.read(bytes))>0)){
                content  = new String(bytes,0,len);
            }

            String line = content.split("\\n")[0];
            String[] arr = line.split("\\s");
            this.method = arr[0];
            this.url = arr[1].split("\\?")[0];

            System.out.println(content);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
}
