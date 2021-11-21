package com.mindbowser.service.impl;

import com.mindbowser.repository.EmployeeDao;
import com.mindbowser.dto.EmployeeRequestDto;
import com.mindbowser.entity.Employee;
import com.mindbowser.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    public List<Employee> findAll() {
        List<Employee> employeeResponse = employeeDao.findAll();
        return employeeResponse;
    }

    @Override
    public Employee save(EmployeeRequestDto employeeRequestDto) {
        Employee employee = new Employee();
        employee.setFirstname(employeeRequestDto.getFirstname());
        employee.setLastname(employeeRequestDto.getLastname());
        employee.setAddress(employeeRequestDto.getAddress());
        employee.setBirthDate(employeeRequestDto.getBirthDate());
        employee.setCity(employeeRequestDto.getCity());
        employee.setMobile(employeeRequestDto.getMobile());
        return employeeDao.save(employee);
    }

    @Override
    public Employee update(EmployeeRequestDto employeeRequestDto,Employee employee) {
        employee.setFirstname(employeeRequestDto.getFirstname());
        employee.setLastname(employeeRequestDto.getLastname());
        employee.setAddress(employeeRequestDto.getAddress());
        employee.setBirthDate(employeeRequestDto.getBirthDate());
        employee.setCity(employeeRequestDto.getCity());
        employee.setMobile(employeeRequestDto.getMobile());
        return employeeDao.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        employeeDao.deleteById(id);
    }
}
