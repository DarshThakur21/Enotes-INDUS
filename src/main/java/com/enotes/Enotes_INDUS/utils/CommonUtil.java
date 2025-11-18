package com.enotes.Enotes_INDUS.utils;

import com.enotes.Enotes_INDUS.handler.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class CommonUtil {
    public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status){
        GenericResponse response=GenericResponse.
                builder().
                responseStatus(status).
                status("Success").
                message("Success").
                data(data).
                build();
        return response.create();
    }

    public static ResponseEntity<?> createBuildResponseMessage( String message, HttpStatus status){
        GenericResponse response=GenericResponse.
                builder().
                responseStatus(status).
                status("Success").
                message(message).
                build();
        return response.create();
    }


    public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status){
        GenericResponse response=GenericResponse.
                builder().
                responseStatus(status).
                status("Failed").
                message("Failed").
                data(data).
                build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status){
        GenericResponse response=GenericResponse.
                builder().
                responseStatus(status).
                status("Failed").
                message(message).
                build();
        return response.create();
    }


    public static String getContentType(String originalFileName) {
        String extension= FilenameUtils.getExtension(originalFileName);
//        List<String> extentions= Arrays.asList("jpg","png","pdf","xlsx","docx");
        switch (extension){
            case "pdf":
                return "applicaton/pdf";
            case "jpg":
                return "image/jpg";
            case "png":
                return "image/png";
            case "xlsx":
                return "applicaton/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "docx":
                return "applicaton/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }

    public static String getUrl(HttpServletRequest httpRequest) {
        String url=httpRequest.getRequestURL().toString();


        url= url.replace(httpRequest.getServletPath()+"/enotes","");


return  url;

    }
}
