package com.pindaodao.autoconfigure.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private String name;

    private String password;

    private String unit;

    private String idCard;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", unit='" + unit + '\'' +
                ", idCard='" + idCard + '\'' +
                '}';
    }
}
