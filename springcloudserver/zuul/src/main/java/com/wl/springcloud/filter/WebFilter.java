package com.wl.springcloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description 自定义路由过滤器
 * @Author 王柳
 * @Date 2019/10/11 8:55
 */
@Slf4j
@Component
public class WebFilter extends ZuulFilter {

    /**
     * 该函数需要返回一个字符串来代表过滤器的类型，而这个类型就是在http请求过程中定义的各个阶段
     * pre: 可以在请求被路由之前被调用
     * routing: 在路由请求时被调用
     * post: 在routing和error 过滤器之后被调用
     * error: 处理请求时发生错误时被调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 通过int的值来定义过滤器的执行顺序，数值越小优先级越高
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 返回一个boolean值来判断该过滤器是否要执行，我们可以通过此方法指定过滤器的有效范围。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * 过滤器的具体逻辑，在该函数中，我们可以实现自定义的过滤器逻辑，来确定是否要拦截当前请求，不对其进行后续的路由，
     * 或是在请求路由返回结果之后对处理的结果进行一些加工等
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        return null;
    }
}
