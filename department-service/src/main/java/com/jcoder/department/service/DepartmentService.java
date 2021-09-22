package com.jcoder.department.service;

import com.jcoder.department.entity.Department;
import com.jcoder.department.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;


    public Department saveDepartment(Department department) {
      log.info("Inside method of DepartmentService - saveDepertment ");
        return departmentRepository.save(department);
    }



    public Department findDepartmentById(Long departmentId) {
        log.info("Inside method of DepartmentService - findDepartmentById");
        return departmentRepository.findByDepartmentId(departmentId);
    }
}
