package org.example.kinobackend.api;



import java.util.List;

public class CreateReservationRequest {

    private int showingId;
    private String customerName;
    private String customerPhone;
    private List<Integer> seatIds;
}