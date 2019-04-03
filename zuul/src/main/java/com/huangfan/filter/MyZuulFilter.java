package com.huangfan.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/** 
 * @Author:huangfan
 * @Date:下午7:45 2019/4/3
 */
public class MyZuulFilter extends ZuulFilter {
    /**
     * spring cloud zuul中实现的过滤器必须包含4个基本特征：
     * 过滤类型，执行顺序，执行条件，具体操作
     */

    /**
     * 过滤类型
     * pre：可以在请求被路由之前调用
     * routing： 路由请求时被调用
     * post：在routing和error过滤器之后被调用
     * 处理请求时发生错误时被调用
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 执行顺序  数值越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }


    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * 可以用来自定义的过滤逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String prefilter01 = request.getParameter("prefilter01");
        System.out.println("执行pre01Filter .....prefilter01=" + prefilter01 	);

        /**
         * 如果用户名和密码都正确，则继续执行下一个filter
         */
        if("true".equals(prefilter01) ){
            /**
             * 会进行路由，也就是会调用api服务提供者
             */
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            /**
             * 可以把一些值放到ctx中，便于后面的filter获取使用
             */
            ctx.set("isOK",true);
        }else{
            /**
             * 不需要进行路由，也就是不会调用api服务提供者
             */
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            /**
             * 可以把一些值放到ctx中，便于后面的filter获取使用
             */
            ctx.set("isOK",false);
            /**
             * 返回内容给客户端  返回错误内容
             */
            ctx.setResponseBody("{\"result\":\"pre01Filter auth not correct!\"}");
        }

        return null;
    }
}
