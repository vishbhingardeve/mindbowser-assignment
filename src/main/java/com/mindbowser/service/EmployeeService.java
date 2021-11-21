package com.mindbowser.service;

import com.mindbowser.dto.EmployeeRequestDto;
import com.mindbowser.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee save(EmployeeRequestDto employee);
    Employee update(EmployeeRequestDto employeeRequestDto,Employee employee);
    void deleteById(Long id);
}
