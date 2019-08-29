package com.leyou.cart.interceptor;


import com.leyou.cart.config.JwtProperties;
import com.leyou.common.pojo.auth.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.auth.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/29
 * @Description: com.leyou.cart.interceptor
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;

    // 定义一个线程域，存放登录用户
    public static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查询token
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        if(StringUtils.isBlank(token)){
            //未登录, 返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        //有token,查询用户信息
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

            //解析成功,放入线程域
            THREAD_LOCAL.set(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 抛出异常，证明未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }


    /**
     * 使用线程池, 任务结束线程不会销毁, 而是回到线程池中,
     * 所以每个请求处理完后要清空ThreadLocal中的值
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }

    public static UserInfo getLoginUser(){
        return THREAD_LOCAL.get();
    }
}


