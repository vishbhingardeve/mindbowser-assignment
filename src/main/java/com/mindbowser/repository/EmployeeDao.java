package com.mindbowser.repository;

import com.mindbowser.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends CrudRepository<Employee, Long> {

    @Query(value = "select * from employee", nativeQuery = true)
    List<Employee> findAll();

    Employee save(Employee employee);

    @Query(value = "select * from employee where id = ?", nativeQuery = true)
    Employee findEmployeeById(Long id);

}