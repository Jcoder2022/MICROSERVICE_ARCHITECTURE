package com.jcoder.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {
    private Long departmentId;
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;
}
