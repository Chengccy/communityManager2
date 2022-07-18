package com.nk.service;

import com.nk.entity.Department;
import com.nk.entity.DepartmentPOVO;
import com.nk.entity.StaffCount;
import com.nk.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService{
    @Autowired
    DepartmentMapper departmentMapper;
    
    public List<Department> getDepartmentByCompanyId(Integer companyId) {
        return departmentMapper.getDepartmentByCompanyId(companyId);
    }

    
    public List<DepartmentPOVO> getDepartment(Integer companyId) {
        return departmentMapper.getDepartment(companyId);
    }

    
    public Boolean addDepartment(Department department) {
        return departmentMapper.addDepartment(department);
    }

    
    public Boolean deleteDepartment(String idStr) {
        return departmentMapper.deleteDepartment(idStr);
    }

    
    public Boolean modifyDepartment(Department department) {
        return departmentMapper.modifyDepartment(department);
    }

    
    public List<StaffCount> getStaffCount(Integer companyId, Integer departmentId) {
        return departmentMapper.getStaffCount(companyId,departmentId);
    }


    public List<Department> getDepartmentByIdStr(String idStr) {
        return departmentMapper.getDepartmentByIdStr(idStr);
    }

    public Department getLastDepartment() {
        return departmentMapper.getLastDepartment();
    }

    public List<Department> getDepartmentByCompanyIdAdd(Integer companyId) {
        return departmentMapper.getDepartmentByCompanyIdAdd(companyId);
    }
}
