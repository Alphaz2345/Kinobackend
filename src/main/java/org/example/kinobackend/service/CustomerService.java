// ─────────────────────────────────────────────────────────
// COPY TIL: service/CustomerService.java
// ─────────────────────────────────────────────────────────
package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.CustomerRepository;
import org.example.kinobackend.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Opret kundekonto (SCRUM-52)
    public Customer register(String fullName, String phone, String email, String password) {
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email er allerede i brug");
        }
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setPhone(phone);
        customer.setEmail(email);
        // I produktion: brug BCrypt til hashing!
        customer.setPasswordHash(password);
        return customerRepository.save(customer);
    }

    // Log ind (SCRUM-53)
    public Customer login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Forkert email eller adgangskode"));
        if (!customer.getPasswordHash().equals(password)) {
            throw new RuntimeException("Forkert email eller adgangskode");
        }
        return customer;
    }

    // Hent kunde
    public Customer getById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));
    }
}
