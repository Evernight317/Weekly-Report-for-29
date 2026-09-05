package com.xcx.filter;


import com.xcx.pojo.Result;
import com.xcx.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;

@Slf4j
//@WebFilter("/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取请求url
        String url=request.getRequestURI();
        log.info("拦截的请求：{}",url);
        // 判断请求url是否包含：login，如果包含，则放行
        if(url.contains("login")){
            log.info("登录操作");
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //获取请求头中的令牌
        String jwt=request.getHeader("token");
        //判断令牌是否存在，如果存在，则放行，不存在则拦截
        if(jwt==null||jwt.isEmpty()){
            log.info("拦截的请求：{}",url);
            Result result= Result.error("NOT_LOGIN");
            //手动转换为json格式
            String json = new ObjectMapper().writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }
        //解析token，解析失败，返回错误结果
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败");
            Result result= Result.error("NOT_LOGIN");
            //手动转换为json格式
            String json = new ObjectMapper().writeValueAsString(result);
            response.getWriter().write(json);
            return;
        }
        //放行
        log.info("令牌合法");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
