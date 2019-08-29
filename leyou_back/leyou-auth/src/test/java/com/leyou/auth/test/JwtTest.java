package com.leyou.auth.test;

import com.leyou.common.pojo.auth.UserInfo;
import com.leyou.common.utils.auth.JwtUtils;
import com.leyou.common.utils.auth.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\leyou\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\leyou\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "luoluo");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2Njg4MjI0N30.k54-OEb5EDe583KsrASSNGm6RByLZ8S83sQjN1O9sChA75Z05A-H_DDBDgaBtl1VPfs12jNn1aKXWE8AcjYcrUbNeMMUq8bIDUeSAQJGP5SxxxsgWYXKZUD5kcPf3UDFp7jhteNDYHX_Z-3mIkw1vYoH4FwOdVMBPml0QMonec0";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}