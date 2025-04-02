package com.umcsuser.carrent.services;

import com.umcsuser.carrent.repositories.RentalRepository;

public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void getRentals(){
        rentalRepository.findAll().forEach(rental -> {
            System.out.println(rental.toString());
        });
    }


}
