package org.example.kinobackend.Controllers;

import org.example.kinobackend.Model.Employee;
import org.example.kinobackend.Service.EmployeeService;
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

    // GET /api/employees — alle medarbejdere
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // POST /api/employees/login — log ind som medarbejder (SCRUM-59)
    @PostMapping("/login")
    public Employee login(@RequestBody Map<String, String> body) {
        return employeeService.login(body.get("email"), body.get("password"));
    }
}
