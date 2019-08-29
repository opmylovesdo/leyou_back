package com.leyou.auth.service;
import java.util.Date;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.common.pojo.auth.UserInfo;
import com.leyou.common.utils.auth.JwtUtils;
import com.leyou.pojo.user.User;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/27
 * @Description: com.leyou.auth.service
 * @version: 1.0
 */
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 认证
     * @param username
     * @param password
     * @return
     */
    public String authentication(String username, String password) {

        try {
            User user = this.userClient.queryUser(username, password).getBody();

            //如果查询结果为null,则直接返回null
            if(user == null){
                return null;
            }

            //如果要查询结果, 则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
