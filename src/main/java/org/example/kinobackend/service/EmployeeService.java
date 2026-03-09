
package org.example.kinobackend.service;

import org.example.kinobackend.repositories.EmployeeRepository;
import org.example.kinobackend.repositories.RoleRepository;
import org.example.kinobackend.model.Employee;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    // Hent alle medarbejdere
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Log ind som medarbejder (SCRUM-59)
    public Employee login(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Forkert email eller adgangskode"));
        if (!employee.getPasswordHash().equals(password)) {
            throw new RuntimeException("Forkert email eller adgangskode");
        }
        return employee;
    }
}
