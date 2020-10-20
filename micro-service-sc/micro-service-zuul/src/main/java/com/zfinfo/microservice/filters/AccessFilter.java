package com.zfinfo.microservice.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
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

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        return null;
    }
}
