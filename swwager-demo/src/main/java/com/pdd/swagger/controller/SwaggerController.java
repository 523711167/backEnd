package com.pdd.swagger.controller;

import com.pdd.swagger.model.HelloWorld;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "入门Demo测试类")
public class SwaggerController {

    @PostMapping("/helloWorld")
    @ApiOperation(value = "欢迎来到Hello World")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 403, message = "无访问权限"),
            @ApiResponse(code = 401, message = "未授权"),
            @ApiResponse(code = 404, message = "未找到")
    })
    public String helloWorld(HelloWorld helloWorld) {
        return "hello World";
    }

    @GetMapping("/helloAdmin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    @ApiOperation(value = "欢迎来到Hello Admin")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 403, message = "无访问权限"),
            @ApiResponse(code = 401, message = "未授权"),
            @ApiResponse(code = 404, message = "未找到")
    })
    public String helloAdmin(String username, String password) {
        return "hello Admin";
    }
}
