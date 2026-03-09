package org.example.kinobackend.RestControllers;

import org.example.kinobackend.model.Customer;
import org.example.kinobackend.model.Ticket;
import org.example.kinobackend.service.CustomerService;
import org.example.kinobackend.service.TicketService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;
    private final CustomerService customerService;

    public TicketController(TicketService ticketService, CustomerService customerService) {
        this.ticketService = ticketService;
        this.customerService = customerService;
    }

    //  — køb billetter direkte (SCRUM-29)
    @PostMapping
    public List<Ticket> purchaseTickets(@RequestBody Map<String, Object> body) {
        int showingId = (int) body.get("showingId");
        List<Integer> seatIds = (List<Integer>) body.get("seatIds");
        Customer customer = null;
        if (body.containsKey("customerId")) {
            customer = customerService.getById((int) body.get("customerId"));
        }
        return ticketService.purchaseTickets(showingId, seatIds, customer);
    }

    //  — slå billet op (SCRUM-36)
    @GetMapping("/{code}")
    public Ticket getTicket(@PathVariable String code) {
        return ticketService.findByCode(code);
    }

    //  — scan ved indgang (SCRUM-34)
    @PostMapping("/{code}/scan")
    public Map<String, Object> scanTicket(@PathVariable String code) {
        boolean success = ticketService.scanTicket(code);
        // SCRUM-35: returnerer success=false hvis allerede brugt
        return Map.of("success", success, "message",
                success ? "Billet godkendt ✓" : "Billet allerede brugt eller ugyldig ✗");
    }

    //  kundens billetter (SCRUM-57)
    @GetMapping("/customer/{customerId}")
    public List<Ticket> getByCustomer(@PathVariable int customerId) {
        return ticketService.getTicketsByCustomer(customerId);
    }
}
