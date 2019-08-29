package com.leyou.auth.client;

import com.leyou.api.user.UserControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/27
 * @Description: com.leyou.auth.client
 * @version: 1.0
 */
@FeignClient("user-service")
public interface UserClient extends UserControllerApi {
}
