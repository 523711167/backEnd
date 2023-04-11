package org.pindaodao.plugin;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Map;

public class CustomDefaultUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) super.extractAuthentication(map);
        authentication.setDetails(null);
        return authentication;
    }
}
