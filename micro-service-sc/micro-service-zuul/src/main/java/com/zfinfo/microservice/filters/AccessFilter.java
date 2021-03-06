package com.zfinfo.microservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zfinfo.microservice.constants.ZuulConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : AccessFilter
 * @Description : 请求接入过滤器
 * @Author : Lyn
 * @CopyRight ZFINFO
 * @Date: 2020-10-20 16:57
 */
@Component
public class AccessFilter extends ZuulFilter {

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
        return 2;
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
        System.out.println("当前请求路径："+currentRequestURI);

        /**下面这一票请求不用走过滤*/
        if (ZuulConstants.PERMISSION_LOGIN.equals(currentRequestURI)
                || ZuulConstants.PERMISSION_REGISTER.equals(currentRequestURI)
                || ZuulConstants.PERMISSION_LOGOUT.equals(currentRequestURI) ) {
            return false;
        }
        return true;
    }



    /**
     * token校验
     * 只有上面返回true的时候，才会进入到该方法
     */
    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //token对象,有可能在请求头传递过来，也有可能是通过参数传过来，实际开发一般都是请求头方式
        String token = request.getHeader("token");

        if (StringUtils.isBlank((token))) {
            token = request.getParameter("token");
        }
        System.out.println("页面传来的token值为：" + token);

        //登录校验逻辑  如果token为null，则直接返回客户端，而不进行下一步接口调用
        if (StringUtils.isBlank(token)) {
            /**不再继续往后传递，而是返回给客户端*/
            requestContext.setSendZuulResponse(false);
            /**返回错误代码*/
            requestContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            requestContext.setResponseBody("{\"result\":\"you are unauthorized.\"}");
        }else{
            requestContext.setSendZuulResponse(true);
            requestContext.setResponseStatusCode(HttpStatus.SC_OK);
        }
        return null;
    }
}
