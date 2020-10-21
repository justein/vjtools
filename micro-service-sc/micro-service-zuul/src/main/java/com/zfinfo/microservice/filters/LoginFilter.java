package com.zfinfo.microservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zfinfo.microservice.constants.ZuulConstants;
import com.zfinfo.microservice.utils.CookieUtils;
import com.zfinfo.microservice.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : LoginFilter
 * @Description : 登录校验过滤器
 * @Author : Lyn
 * @CopyRight ZFINFO
 * @Date: 2020-10-21 14:33
 */
@Component
public class LoginFilter extends ZuulFilter {

    /**
     * 过滤器类型，前置过滤器
     *  filter类型,分为pre、error、post、 route
     *  pre: 请求执行之前filter
     * route: 处理请求，进行路由
     * post: 请求处理完成后执行的filter
     * error: 出现错误时执行的filter
     */

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * filter执行顺序，通过数字指定，数字越小，执行顺序越先
     * @return
     */
    @Override
    public int filterOrder() {
        return 3;
    }

    /**
     * 过滤器是否生效
     * 返回true代表需要本过滤器进行过滤，返回false代表不需要本过滤器进行过滤
     */
    @Override
    public boolean shouldFilter() {
        //共享RequestContext，上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String currentRequestURI = request.getRequestURI();
        System.out.println("在LoginFilter中，当前请求路径为："+currentRequestURI);

        //不需要登录校验的URL
        //不需要token校验的URL
        /**下面这一票请求不用走过滤*/
        if (ZuulConstants.PERMISSION_LOGIN.equals(currentRequestURI)
                || ZuulConstants.PERMISSION_REGISTER.equals(currentRequestURI)
                || ZuulConstants.PERMISSION_LOGOUT.equals(currentRequestURI) ) {
            return false;
        }

        return true;
    }

    /**
     * 业务逻辑
     * 只有上面返回true的时候，才会进入到该方法
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response=requestContext.getResponse();

        //取用户token
        String userToken = null;

        userToken = CookieUtils.getCookieValue(request,"cookie_token_key");
        if (StringUtils.isBlank(userToken)) {
            userToken = request.getHeader("token");
        }

            try {
                /**取不到用户token，则重定向到登录页*/
                if (StringUtils.isBlank(userToken)) {
                    response.sendRedirect(request.getRequestURL() + "?redirect=" + HttpUtils.getBaseURL(request));
                    requestContext.setSendZuulResponse(false);
                    requestContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
//                    return false;
                }else {
                    /**如果能取到用户token，则说明用户已经登陆过了*/
                    /**需要搞一个登录认证的服务，把用户token放进redis*/
                    /**网关从redis取userToken*/

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        return null;
    }
}
