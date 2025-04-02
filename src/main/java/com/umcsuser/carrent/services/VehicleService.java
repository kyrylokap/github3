package com.umcsuser.carrent.services;

import com.umcsuser.carrent.models.Rental;
import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.repositories.RentalRepository;
import com.umcsuser.carrent.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    public void deleteVehicle(String id){
        vehicleRepository.deleteById(id);
    }
    public void addVehicle(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }
    public void findById(String id){
        vehicleRepository.findById(id);
    }

    public void getAllVehicles(){
        vehicleRepository.findAll().forEach(v->{
            System.out.println(v.toString());
        });
    }



    public void returnVehicle(){
        Rental rental = rentalRepository.findAll().stream().filter(v ->
                        rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getVehicleId()).isPresent())
                .findFirst().get();
        rental.setReturnDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        rentalRepository.save(rental);
    }

    public void rentVehicle(String id, User user){
        Vehicle v = vehicleRepository.findById(id).get();
        rentalRepository.save(new Rental(null,v.getId()
                , user.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) ,null));

    }
}
