package com.gupaoedu.vip.bio.tomcat.http;

import java.io.IOException;
import java.io.OutputStream;

public class GPResponse {
    private OutputStream outputStream;
    public GPResponse(OutputStream os){
        this.outputStream = os;

    }

    public void write(String meg) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type:text/html;\n")
                .append("\r\n")
                .append(meg);
        outputStream.write(sb.toString().getBytes());
    }
}
