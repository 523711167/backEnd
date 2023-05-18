package org.pindaodao.pengxiaoxi;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Override
    public String generateTokenByHMAC(String payloadStr, String secret) {
        try {
            //准备JWS-header
            JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT).build();
            //将负载信息装载到payload
            Payload payload = new Payload(payloadStr);
            //封装header和payload到JWS对象
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            //创建HMAC签名器
            JWSSigner jwsSigner = new MACSigner(secret);
            //签名
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PayloadDto getDefaultPayloadDto() {
        Date now = new Date();
        Date exp = new Date();
        return PayloadDto.builder()
                .sub("zsh")
                .iat(now.getTime())
                .exp(exp.getTime())
                .jti(UUID.randomUUID().toString())
                .username("zsh")
                .authorities(Collections.singletonList("ADMIN"))
                .build();
    }

    @Override
    public PayloadDto verifyTokenByHMAC(String token, String secret) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            //创建HMAC验证器
            JWSVerifier jwsVerifier = new MACVerifier(secret);
            if (!jwsObject.verify(jwsVerifier)) {
                throw new Exception("token签名不合法!");
            }
            String payload = jwsObject.getPayload().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            PayloadDto payloadDto = objectMapper.readValue(payload, PayloadDto.class);
            return payloadDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RSAKey loadJKSByClassPath() {
        //从类路径下加载证书
        KeyPair keyPair = generateRsaKey();
        //获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    }

    @Override
    public String generateTokenByRSA(String payloadStr, RSAKey rsaKey) throws JOSEException {
        //构建JWS头
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();
        //构建载荷
        Payload payload = new Payload(payloadStr);
        //将JWS-header和payload封装成JWS对象中
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //创建签名器
        JWSSigner signer = new RSASSASigner(rsaKey, true);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    @Override
    public PayloadDto verifyTokenByRSA(String token, RSAKey rsaKey) throws Exception {
        JWSObject jwsObject = JWSObject.parse(token);
        RSAKey verifyKey = rsaKey.toPublicJWK();
        JWSVerifier verifier = new RSASSAVerifier(verifyKey);
        if (!jwsObject.verify(verifier)) {
            throw new Exception("签名不合法!");
        }
        String payload = jwsObject.getPayload().toString();
        String substring = payload.substring(11, payload.length() - 1);
        String[] strings = substring.split(",");
        PayloadDto payloadDto = PayloadDto.builder()
                .sub(strings[0])
                .iat(100L)
                .exp(100L)
                .jti(strings[3])
                .username(strings[4])
                .authorities(Collections.singletonList(strings[5]))
                .build();
        return payloadDto;
    }


    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}
