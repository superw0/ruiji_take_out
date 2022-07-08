package com.wang.ruiji_take_out.interceptor;


import com.alibaba.fastjson.JSON;
import com.wang.ruiji_take_out.common.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("employee")!=null){
            return true;
        }else{
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            response.sendRedirect("/backend/page/login/login.html");
            return true;
        }
    }
}
