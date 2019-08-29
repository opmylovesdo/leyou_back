package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.pojo.user.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/26
 * @Description: com.leyou.user.service
 * @version: 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String KEY_PREFIX = "user:verify:";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type 1，用户名；2，手机
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        if (type == null) {
            type = 1;
        }

        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }

        return this.userMapper.selectCount(record) == 0;

    }

    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone) {

        if (StringUtils.isNoneBlank(phone)) {
            //生成验证码
            String code = NumberUtils.generateCode(6);
            try {


                //发送消息给rabbitMQ
                Map<String, String> data = new HashMap<>();
                data.put("phone", phone);
                data.put("code", code);
                amqpTemplate.convertAndSend("leyou.sms.exchange", "sms.verify.code", data);

                //保存验证码到Redis
                this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);

                return true;
            } catch (AmqpException e) {
                LOGGER.error("发送短信失败。phone：{}， code：{}", phone, code);
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * @param user
     * @param code 短信验证码
     * @return
     */
    public Boolean register(User user, String code) {
        //查询redis中的验证码 和前端发送的code比较
        String redisCode = redisTemplate.boundValueOps(KEY_PREFIX + user.getPhone()).get();
        //1.校验验证码
        if (!StringUtils.equals(code, redisCode)) {
            return false;
        }
        //2.生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //3.加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        //4.新增用户

        //强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());

        boolean b = this.userMapper.insertSelective(user) == 1;

        if(b){
            //注册成功, 删除redis中的记录
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }

        return b;
    }

    /**
     * 根据参数中的用户名和密码查询指定用户
     *
     * @param username
     * @param password
     * @return
     */
    public User queryUserByUsernameAndPassword(String username, String password) {
        //根据用户查询用户
        User record = new User();
        record.setUsername(username);

        User user = this.userMapper.selectOne(record);
        if(user == null) return null;

        //校验密码
        if(!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))){
            return null;
        }

        // 用户名密码都正确
        //返回查询对象
        return user;
    }
}
