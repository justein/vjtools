package com.zfinfo.microservice.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : HttpUtils
 * @Description : Http请求相关的工具封装
 * @Author : Lyn
 * @CopyRight ZFINFO
 * @Date: 2020-10-21 15:11
 */
public class HttpUtils {

    /**
     * 这是获取需要传递的url，以便后续用户登录后直接跳转到这个页面
     * @param request
     * @return
     */
    public static String getBaseURL(HttpServletRequest request) {
        String url = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath()
                + request.getRequestURI();
        return url;
    }

}
