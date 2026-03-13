package org.example.kinobackend.service;

import org.example.kinobackend.Repositories.CustomerRepository;
import org.example.kinobackend.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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

    // Log ud (SCRUM-54)
    public String logout() {
        return "Customer logged out";
    }

    // Glemt adgangskode - opret reset token
    public String requestPasswordReset(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));

        String token = UUID.randomUUID().toString();

        customer.setResetToken(token);
        customer.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        customerRepository.save(customer);

        return token;
    }

    // Nulstil adgangskode Scrum 5
    public String resetPassword(String token, String newPassword) {
        Customer customer = customerRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Ugyldigt reset-token"));

        if (customer.getResetTokenExpiry() == null ||
                customer.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset-token er udløbet");
        }

        customer.setPasswordHash(newPassword);
        customer.setResetToken(null);
        customer.setResetTokenExpiry(null);

        customerRepository.save(customer);

        return "Adgangskode er opdateret";
    }

    // Hent kunde
    public Customer getById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kunde ikke fundet"));
    }
}