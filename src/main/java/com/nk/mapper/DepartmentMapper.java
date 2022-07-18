package com.nk.mapper;

import com.nk.entity.Department;
import com.nk.entity.DepartmentPOVO;
import com.nk.entity.StaffCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    List<Department> getDepartmentByCompanyId(@Param("companyId") Integer companyId);

    List<DepartmentPOVO> getDepartment(@Param("companyId") Integer companyId);

    Boolean addDepartment(Department department);

    Boolean deleteDepartment(@Param("idStr") String idStr);

    Boolean modifyDepartment( Department department);

    List<StaffCount> getStaffCount(@Param("companyId")Integer companyId, @Param("departmentId") Integer departmentId);

    List<Department> getDepartmentByIdStr(@Param("idStr") String idStr);

    Department getLastDepartment();

    List<Department> getDepartmentByCompanyIdAdd(@Param("companyId") Integer companyId);
}