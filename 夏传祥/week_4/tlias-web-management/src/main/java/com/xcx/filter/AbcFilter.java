package com.xcx.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
//
//@WebFilter("/*")
public class AbcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Abc拦截到了请求...放行前");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Abc放行后");
    }
}
