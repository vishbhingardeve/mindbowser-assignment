package com.mindbowser.controller;

import com.mindbowser.repository.EmployeeDao;
import com.mindbowser.dto.ApiMsgResponse;
import com.mindbowser.dto.EmployeeRequestDto;
import com.mindbowser.entity.Employee;
import com.mindbowser.exception.BadRequestException;
import com.mindbowser.service.EmployeeService;
import com.mindbowser.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employee")
@Slf4j
public class ManagerController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDao employeeDao;

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Add new employee.", description = "Add new employee in list. (All fields required)", tags = {"Manager Dashboard"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public ResponseEntity<ApiMsgResponse>  addNewEmployee(@RequestBody @Valid EmployeeRequestDto employeeRequestDto){
        log.info("Enter: addNewEmployee Method Type: GET: Request Arguments: {}", employeeRequestDto);
        if (employeeRequestDto.getAddress().isEmpty() || employeeRequestDto.getCity().isEmpty() || employeeRequestDto.getFirstname().isEmpty() ||
                employeeRequestDto.getLastname().isEmpty() || employeeRequestDto.getMobile().isEmpty() || employeeRequestDto.getBirthDate() == null) {
            throw new BadRequestException("Please provide all inputs.");
        }
        Employee employeeResponse = employeeService.save(employeeRequestDto);
        log.info("Exit: addNewEmployee Method Type: GET: Response Arguments: {}", employeeResponse);
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), "Employee added successfully.", employeeResponse));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get all employee details.", description = "Get all employee details.", tags = {"Manager Dashboard"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value = "/getAllEmployee", method = RequestMethod.GET)
    public ResponseEntity<ApiMsgResponse> getAllEmployee(){
        log.info("Enter: getAllEmployee Method Type: GET: Request Arguments: {}", "Get all employee");
        List<Employee> employeeResponse = employeeService.findAll();
        log.info("Exit: getAllEmployee Method Type: GET: Response Arguments: {}", employeeResponse);
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), Constants.SUCCESS, employeeResponse));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update employee by id.", description = "Update employee details by id. (All fields required)", tags = {"Manager Dashboard"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value = "/updateEmployee/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ApiMsgResponse>  updateEmployee(@PathVariable("id") long id, @RequestBody @Valid EmployeeRequestDto employeeRequestDto){
        log.info("Enter: updateEmployee Method Type: GET: Request Arguments: {}", employeeRequestDto);
        Employee employee  = employeeDao.findEmployeeById(id);
        if (employee == null) {
            throw new BadRequestException("Employee not exist with id : "+id);
        }
        if (employeeRequestDto.getAddress().isEmpty() || employeeRequestDto.getCity().isEmpty() || employeeRequestDto.getFirstname().isEmpty() ||
                employeeRequestDto.getLastname().isEmpty() || employeeRequestDto.getMobile().isEmpty() || employeeRequestDto.getBirthDate() == null) {
            throw new BadRequestException("Please provide all inputs.");
        }
        employeeService.update(employeeRequestDto, employee);
        log.info("Exit: updateEmployee Method Type: GET: Response Arguments: {}", employeeRequestDto);
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), "Employee updated successfully.", employeeRequestDto));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete employee by id.", description = "Update employee details by id.", tags = {"Manager Dashboard"}, security = @SecurityRequirement(name = "bearerAuth"))
    @RequestMapping(value = "/deleteEmployee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiMsgResponse>  deleteEmployee(@PathVariable("id") long id){
        log.info("Enter: deleteEmployee Method Type: GET: Request Arguments: {}", id);
        Employee employee  = employeeDao.findEmployeeById(id);
        if (employee == null) {
            throw new BadRequestException("Employee not exist with id : "+id);
        }
        employeeService.deleteById(id);
        log.info("Exit: deleteEmployee Method Type: GET: Response Arguments: {}", "Deleted");
        return ResponseEntity.ok(new ApiMsgResponse(HttpStatus.OK.value(), Constants.SUCCESS, "Employee deleted successfully."));
    }
}
