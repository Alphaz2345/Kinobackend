package org.example.kinobackend.RestController;

import org.example.kinobackend.model.Customer;
import org.example.kinobackend.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // opret konto (SCRUM-52)
    @PostMapping("/c")
    public Customer register(@RequestBody Map<String, String> body) {
        return customerService.register(
                body.get("fullName"),
                body.get("phone"),
                body.get("email"),
                body.get("password")
        );
    }

    //log ind (SCRUM-53)
    @PostMapping("/login")
    public Customer login(@RequestBody Map<String, String> body) {
        return customerService.login(body.get("email"), body.get("password"));
    }

    //Scrum 54 Log en kunde ud
    @PostMapping("/logout")
    public String logout() {
        return "Customer logged out";
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id) {
        return customerService.getById(id);
    }
}