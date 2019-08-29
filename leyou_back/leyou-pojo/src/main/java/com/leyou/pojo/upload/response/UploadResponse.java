package com.leyou.pojo.upload.response;

import com.leyou.common.pojo.response.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.pojo.upload.response
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse implements Response {
    //文件保存的地址
    private String url;
}
