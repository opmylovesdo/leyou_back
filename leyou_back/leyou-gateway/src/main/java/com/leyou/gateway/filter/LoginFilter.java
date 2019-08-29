package com.leyou.gateway.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.auth.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/27
 * @Description: com.leyou.gateway.filter
 * @version: 1.0
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        //判断白名单
        //遍历允许路径
//        return this.filterProperties.getAllowPaths().stream()
//                .filter(path -> requestURI.startsWith(path)).count() > 0;
        for (String path : this.filterProperties.getAllowPaths()) {
            // 然后判断是否是符合
            if (requestURI.startsWith(path)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());


        if(StringUtils.isBlank(token)){
            setBadResponse(context);
        }

        // 校验
        try {
            // 校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            setBadResponse(context);
            LOGGER.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
        }
        return null;
    }

    private void setBadResponse(RequestContext context) {
        //设置为false 不转发请求
        context.setSendZuulResponse(false);
        // 校验出现异常，返回403
        context.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
    }
}
