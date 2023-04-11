package com.pindaodao.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "pindaodao"
)
@Data
public class PindaodaoProperties {

    private String name = "wangWu";

    private String password = "123";

    private String unit = "蒸湘区";

    private String idCard = "430408XXXXXXXXXXXXX";
}
