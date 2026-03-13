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

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/login")
    public Employee login(@RequestBody Map<String, String> body) {
        return employeeService.login(body.get("email"), body.get("password"));
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Map<String, String> body) {
        return employeeService.createEmployee(body.get("fullname"), body.get("email"), body.get("role"));
    }
}