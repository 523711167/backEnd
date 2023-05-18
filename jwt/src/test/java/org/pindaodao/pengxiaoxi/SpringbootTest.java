package org.pindaodao.pengxiaoxi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pindaodao.JwtApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = JwtApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringbootTest {

    @Autowired
    private JwtTokenService jwtTokenServiceImpl;

    /**
     * 对称加密
     */
    @Test
    public void test() throws JsonProcessingException, KeyLengthException {
        ObjectMapper objectMapper=new ObjectMapper();

        PayloadDto payloadDto = jwtTokenServiceImpl.getDefaultPayloadDto();
        String token = jwtTokenServiceImpl.generateTokenByHMAC(objectMapper.writeValueAsString(payloadDto), getMD5Str("TESSSSSSSSSSSSSSSSS"));
        System.out.println(token);
    }

    @Test
    public void test1() throws JsonProcessingException, KeyLengthException {
        ObjectMapper objectMapper=new ObjectMapper();

        PayloadDto payloadDto = jwtTokenServiceImpl.verifyTokenByHMAC("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                        ".eyJzdWIiOiJ6c2giLCJpYXQiOjE2ODQ0MTM3NTE0MjYsImV4cCI6MTY4NDQxMzc1MTQyNiwianRpIjoiZWUyNzQzZTQtMTZkOS00Y2E0LWI3MTEtZjZjNDY3ZjU0YzI0IiwidXNlcm5hbWUiOiJ6c2giLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdfQ" +
                        ".k0IfvDmlVaYfszndA_pIcxP625LpEMox1tNv19r_RtU",
                getMD5Str("TESSSSSSSSSSSSSSSSS"));
        System.out.println(objectMapper.writeValueAsString(payloadDto));
    }

    /**
     * 非对称加密
     */
    @Test
    public void test2() {
        RSAKey key = jwtTokenServiceImpl.loadJKSByClassPath();
        System.out.println(new JWKSet(key).toJSONObject());
    }

    @Test
    public void test3() throws JOSEException {
        PayloadDto payloadDto = jwtTokenServiceImpl.getDefaultPayloadDto();
        RSAKey rsaKey = jwtTokenServiceImpl.loadJKSByClassPath();
        String token = jwtTokenServiceImpl.generateTokenByRSA(payloadDto.toString(), rsaKey);
        System.out.println(token);
    }

    /**
     * 公钥是临时生成，所以解密一直失败
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        PayloadDto payloadDto = jwtTokenServiceImpl.verifyTokenByRSA("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.UGF5bG9hZER0byhzdWI9enNoLCBpYXQ9MTY4NDQxNTQxODE0OSwgZXhwPTE2ODQ0MTU0MTgxNDksIGp0aT0yZjU4N2FiNS00Zjc5LTQ3MDMtODhmYy1hMWM1NGNkODQ5YWUsIHVzZXJuYW1lPXpzaCwgYXV0aG9yaXRpZXM9W0FETUlOXSk.Y3Wo9N2bMcI9vtTi35oVt0jkyV8KkkYcbTM-DCkIf3SqmXewhiedBc58RTkLHV9aQ2hvqwqFnADoizba5kwN-ezHXgZ2ghJCfAHlDF6_OqJXXdVfkauhqSCJWICP1Rgf8y6lf6LlGP61rHIEGj1XRtViSSsYHNJ7CsXjLjqqxTCwztAL0Fybt494b71CINjQ2ATWjQoHX_YxyUaylHO-UEJ8xJZ7mT_Mng1LXAVnZFnpwKC-G4Z49aeuhq3kRBxOk956LQU1GipQXE9uNetHwtvP_r_Txk-FZO7T4PV_HA_LjD4QfNr4zi1770rUcaJX0qDSbgn62_vxgzkzAuCRUA", jwtTokenServiceImpl.loadJKSByClassPath());
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(payloadDto));

    }

    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        return new BigInteger(1, digest).toString(16);
    }
}
