package org.pindaodao.pengxiaoxi;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.jwk.RSAKey;

import java.text.ParseException;

public interface JwtTokenService {

    /**
     * 使用HMAC对称加密算法生成token
     */
    String generateTokenByHMAC(String payloadStr, String secret) throws KeyLengthException;

    /**
     * 模拟生成用户数据
     */
    PayloadDto getDefaultPayloadDto();

    /**
     * 验证令牌
     */
    PayloadDto verifyTokenByHMAC(String token, String secret);


    /**
     * 从类路径下加载jwt.jks
     */
    RSAKey loadJKSByClassPath();

    /**
     * 使用RSA非对称算法生成token
     */
    String generateTokenByRSA(String payloadStr, RSAKey rsaKey) throws JOSEException;

    /**
     * 根据RSA非对称算法验证token
     */
    PayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws Exception;
}
