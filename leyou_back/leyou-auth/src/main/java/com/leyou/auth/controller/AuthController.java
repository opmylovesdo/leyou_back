package com.leyou.auth.controller;

import com.leyou.api.auth.AuthControllerApi;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.auth.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.auth.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/27
 * @Description: com.leyou.auth.controller
 * @version: 1.0
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController implements AuthControllerApi {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AuthService authService;

    /**
     * 认证
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        //登陆校验
        String token = this.authService.authentication(username, password);

        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //将token写入cookie,并指向httpOnly为true, 防止通过JS获取和修改
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),
                token, jwtProperties.getCookieMaxAge(), null, true);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(
            @CookieValue("LY_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            // 从token中解析token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

            if(userInfo == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            //解析成功要重新刷新token (cookie的有效时间和token的有效时间)
            token = JwtUtils.generateToken(userInfo,
                    this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());

            //更新cookie中的token
            CookieUtils.setCookie(request, response,
                    this.jwtProperties.getCookieName(), token,
                    this.jwtProperties.getCookieMaxAge());

            // 解析成功返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则，响应500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
}
