package com.cn.DTO;

import lombok.Data;

@Data
public class RegisterEnterpriseDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String companyName;
}
