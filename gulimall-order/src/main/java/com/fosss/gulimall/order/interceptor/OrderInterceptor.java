package com.fosss.gulimall.order.interceptor;

import com.fosss.common.constant.CartConstant;
import com.fosss.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.fosss.common.constant.AuthServerConstant.LOGIN_USER;

/**
 * @author: fosss
 * Date: 2023/6/2
 * Time: 22:54
 * Description:
 */
@Component
public class OrderInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据是否登录来判断是否拦截
        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(LOGIN_USER);
        if (attribute == null) {
            //没登录,进行拦截，跳转页面返回错误信息
            response.sendRedirect("http://auth.gulimall.com/login.html");
            request.getSession().setAttribute("msg", "请先进行登录");
            return false;
        } else {
            //登录了,将用户信息放到线程中
            threadLocal.set(attribute);
            return true;
        }
    }
}


















