package org.pindaodao.pengxiaoxi;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JjwtTest {

    /**
     * 在不使用Jjwt的情况下生成jwt
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void test() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //1。定义
        String header = "{\"alg\":\"HS256\"}";
        String claims = "{\"sub\":\"NBB\"}";
        Base64.Encoder encoder = Base64.getEncoder();

        String encodedHeader = encoder.encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedClaims = encoder.encodeToString(claims.getBytes(StandardCharsets.UTF_8));
        //2。header+payload
        String concatenated = encodedHeader + '.' + encodedClaims;

        //3。选择密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        //3。选择对应摘要(摘要)算法
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(concatenated.getBytes());
        byte[] result = messageDigest.digest();

        //4。合成JWT令牌
        String jws = concatenated + '.' + encoder.encodeToString(result);
    }

    /**
     * 使用HS256获取对应密钥
     */
    @Test
    public void test1() {
        //或HS384或HS512
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Test
    public void test2() {
        //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PrivateKey aPrivate = keyPair.getPrivate();
        String jws = Jwts.builder()
                //发行方
                .setIssuer("Pengxiaoxi")
                //声明
                .setSubject("Bob")
                //受众人群
                .setAudience("PengJing")
                //到期时间
                //.setExpiration(new Date())
                //声明不早于时间
                .setNotBefore(new Date())
                //签发时间
                .setIssuedAt(new Date())
                //JWT ID
                .setId(UUID.randomUUID().toString())
                .signWith(aPrivate, SignatureAlgorithm.RS256)
                .compact();
        System.out.println("jws = " + jws);
        PublicKey aPublic = keyPair.getPublic();

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(aPublic)
                .build()
                .parseClaimsJws(jws);
        System.out.println("验证JWS");
    }
}
