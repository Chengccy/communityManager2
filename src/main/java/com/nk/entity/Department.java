package com.nk.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Integer departmentId;
    private String departmentName;
    private Integer companyId;
    private Integer status;

}
