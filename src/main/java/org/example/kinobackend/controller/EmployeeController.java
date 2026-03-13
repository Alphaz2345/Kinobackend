package org.example.kinobackend.controller;

import org.example.kinobackend.model.Employee;
import org.example.kinobackend.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // alle medarbejdere
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // log ind som medarbejder (SCRUM-59)
    @PostMapping("/login")
    public Employee login(@RequestBody Map<String, String> body) {
        return employeeService.login(body.get("email"), body.get("password"));
    }
    @PostMapping
    public Employee createEmployee(@RequestBody Map<String, String> body) {
        return employeeService.createEmployee(body.get("fullname"), body.get("email"), body.get("role"));
    }
    @GetMapping
    public List<Employee>getAllEmployeesWithRole() {
        return employeeService.getAllEmployees();
    }
}
