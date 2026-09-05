package com.xcx.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter("/*")
public class DemoFilter implements Filter {
    @Override//初始化方法，只调用一次
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("demo拦截到了请求");
        //放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("demo放行后");
    }

    @Override//销毁方法，只调用一次
    public void destroy() {
        Filter.super.destroy();
    }
}
