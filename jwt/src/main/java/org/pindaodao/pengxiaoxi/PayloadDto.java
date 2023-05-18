package org.pindaodao.pengxiaoxi;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class PayloadDto {
    /**
     * 主题
     */
    private String sub;
    /**
     * 签发时间
     */
    private Long iat;
    /**
     * 过期时间
     */
    private Long exp;
    /**
     * JWT ID
     */
    private String jti;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户权限
     */
    private List<String> authorities;
}
