package com.leyou.sms.test;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/26
 * @Description: com.leyou.sms.test
 * @version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSendMsg {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private SmsUtils smsUtils;


    @Test
    public void testSendMsg() throws ClientException {
        SendSmsResponse sendSmsResponse = smsUtils.sendSms("18823873606", "222333", smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());
        System.out.println(sendSmsResponse);

    }


    @Test
    public void testStringUtils(){
        String a = "a  b";
        System.out.println(StringUtils.isNoneBlank(a));
    }


}
