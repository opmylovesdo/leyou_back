package com.leyou.api.user;

import com.leyou.pojo.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/26
 * @Description: com.leyou.api.user
 * @version: 1.0
 */
@Api("用户服务接口")
public interface UserControllerApi {

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    @ApiOperation("校验数据是否可用")
    ResponseEntity<Boolean> checkUserData(@PathVariable(value = "data") String data,
                                          @PathVariable(value = "type", required = false) Integer type);

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    @ApiOperation("发送手机验证码")
    ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone);

    /**
     *
     * @param user
     * @param code 短信验证码
     * @return
     */
    @PostMapping("register")
    @ApiOperation("注册")
    ResponseEntity<Void> register(User user, @RequestParam("code") String code);

    /**
     * 根据参数中的用户名和密码查询指定用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    @ApiOperation("根据参数中的用户名和密码查询指定用户")
    ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password);

}
