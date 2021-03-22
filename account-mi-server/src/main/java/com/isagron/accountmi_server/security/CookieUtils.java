package com.isagron.accountmi_server.security;

import com.isagron.accountmi_server.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;


@Service
public class CookieUtils {


    @Autowired
    private SecurityProperties restSecProps;

    public static HttpCookie getCookie(ServerHttpRequest request, String session) {
        MultiValueMap<String, HttpCookie> cookeis = request.getCookies();
        return null;
    }

    public HttpCookie getCookie(String name){
        return null;
    }
}
