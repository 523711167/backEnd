package org.pindaodao.controller;

import io.swagger.annotations.ApiOperation;
import org.pindaodao.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/")
public class HelloController {

    @PostMapping("liming")
    @ApiOperation(value = "欢迎来到Java")
    public Object HelloWorld(UserDto userDto) {
        return new UserDto();
    }

}
