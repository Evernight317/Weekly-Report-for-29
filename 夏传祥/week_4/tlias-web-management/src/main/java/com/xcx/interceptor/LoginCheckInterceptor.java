package com.xcx.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcx.pojo.Result;
import com.xcx.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override//目标资源方法运行前运行，返回true放行，返回false拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.printf("preHandle执行");


        // 获取请求url
        String url=request.getRequestURI();
        log.info("拦截的请求：{}",url);
        // 判断请求url是否包含：login，如果包含，则放行
        if(url.contains("login")){
            log.info("登录操作");
            return true;
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
            return false;
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
            return false;
        }
        //放行
        log.info("令牌合法");
        return true;
    }

    @Override//目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle运行");

    }

    @Override//最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("afterCompletion运行");
    }

}
