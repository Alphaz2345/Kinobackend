package org.example.kinobackend.Repositories;

import org.example.kinobackend.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);
}
