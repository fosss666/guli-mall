package com.fosss.gulimall.cart.interceptor;

import com.fosss.common.constant.AuthServerConstant;
import com.fosss.common.constant.CartConstant;
import com.fosss.common.vo.MemberRespVo;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author: fosss
 * Date: 2023/5/22
 * Time: 21:24
 * Description:拦截器
 */
public class GulimallInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal;

    /**
     * 执行前的业务处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //判断是否登录,获取用户信息
        MemberRespVo member = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        UserInfoTo userInfoTo = new UserInfoTo();
        if (member != null) {
            //已经登录，设置用户id
            userInfoTo.setUserId(member.getId());
        }
        //根据cookie中是否有"user-key"判断是否有临时用户
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (CartConstant.TEMP_USER_KEY_NAME.equals(name)) {
                    //标记有临时用户
                    userInfoTo.setIsTempUser(true);
                }
            }
        }
        //如果没有userKey要设置一个
        if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        //保存到当前线程中
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 执行后的业务处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        //如果cookie中没有临时用户要创建一个
        if (!userInfoTo.getIsTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_KEY_NAME, userInfoTo.getUserKey());
            cookie.setDomain(CartConstant.TEMP_USER_KEY_COOKIE_DOMAIN);
            cookie.setMaxAge(CartConstant.TEMP_USER_KEY_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}



















