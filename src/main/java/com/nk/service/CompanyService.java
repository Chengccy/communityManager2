package com.nk.service;

import com.nk.entity.Company;
import com.nk.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CompanyService {
    @Autowired
    private CompanyMapper companyMapper;
    public List<Company> getAllCompany() {
        return companyMapper.getAllCompany();
    }

    
    public Boolean deleteCompanyById(String idStr) {
        return companyMapper.deleteCompanyById(idStr);
    }

    
    public List<Company> getCompanyByName(String companyName) {
        return companyMapper.getCompanyByName(companyName);
    }

    
    public Boolean addCompany(Company company) {
       return companyMapper.addCompany(company);
    }

    
    public List<Company> getCompanyById(Integer id) {
        return companyMapper.getCompanyById(id);
    }

    
    public Boolean modifyCompany(Company company) {
        return companyMapper.modifyCompany(company);
    }

    public List<Company> getCompanyByIdStr(String idStr) {
        return companyMapper.getCompanyByIdStr(idStr);
    }

    public List<Company> getAllCompanyAdd() {
        return companyMapper.getAllCompanyAdd();
    }
}
