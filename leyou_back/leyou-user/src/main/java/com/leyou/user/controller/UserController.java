package com.leyou.user.controller;

import com.leyou.api.user.UserControllerApi;
import com.leyou.pojo.user.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/26
 * @Description: com.leyou.user.controller
 * @version: 1.0
 */
@RestController
public class UserController implements UserControllerApi {

    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type 1，用户名；2，手机
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable(value = "data") String data,
                                                 @PathVariable(value = "type", required = false) Integer type) {

        Boolean bool = this.userService.checkData(data, type);
        if (null == bool) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }


    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        Boolean bool = this.userService.sendVerifyCode(phone);
        if(bool == null || !bool){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 注册
     *
     * @param user
     * @param code 短信验证码
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        Boolean bool = this.userService.register(user, code);
        if(bool == null || !bool){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据参数中的用户名和密码查询指定用户
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        User user = this.userService.queryUserByUsernameAndPassword(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

}
