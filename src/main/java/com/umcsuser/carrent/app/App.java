package com.umcsuser.carrent.app;

import com.umcsuser.carrent.models.User;
import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.services.AuthService;
import com.umcsuser.carrent.services.RentalService;
import com.umcsuser.carrent.services.VehicleService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run(){
        User user;
        String login,haslo;


        while (true){
            System.out.println("Podaj login:");
            login = scanner.nextLine();
            System.out.println("Podaj haslo:");
            haslo = scanner.nextLine();
            if (authService.register(login,haslo,"USER")){
                System.out.println("Udalo sie zarejestrowac");
                System.out.println("Teraz musisz zalogowac");
            }
            if (authService.login(login,haslo).isPresent()){
                user = authService.login(login,haslo).get();
                System.out.println("Udalo sie zalogowac sie");
                break;
            }

        }

        while(true) {
            if (user.getRole().equals("ADMIN")) {
                System.out.println("1.Add vehicle");
                System.out.println("2.Remove vehicle");
                System.out.println("3.Show all vehicles");
                System.out.println("4.Show users");
                System.out.println("5.Show history rented cars");
                int c = scanner.nextInt();
                scanner.nextLine();
                if (c == 1) {
                    Vehicle vehicle = new Vehicle();
                    System.out.println("Id:");
                    vehicle.setId(scanner.nextLine());
                    System.out.println("Category:");
                    vehicle.setCategory(scanner.nextLine());
                    System.out.println("Brand:");
                    vehicle.setBrand(scanner.nextLine());
                    System.out.println("Model:");
                    vehicle.setModel(scanner.nextLine());
                    System.out.println("Year:");
                    vehicle.setYear(Integer.decode(scanner.nextLine()));
                    System.out.println("Plate:");
                    vehicle.setPlate(scanner.nextLine());
                    System.out.println("Price:");
                    vehicle.setPrice(Double.parseDouble(scanner.nextLine()));
                    String key = null,val;
                    Map<String,Object> m = new HashMap<>();
                    System.out.println("Print stop if you want to stop :)");
                    while(true){
                        System.out.println("Key:");
                        key = scanner.nextLine();
                        if(key.equals("stop")){
                            break;
                        }
                        System.out.println("Value:");
                        val = scanner.nextLine();
                        m.put(key,val);
                    }
                    vehicle.setAttributes(m);
                    vehicleService.addVehicle(vehicle);
                    System.out.println("Dodano" + vehicle);
                }
                if (c == 2){
                    vehicleService.getAllVehicles();
                    System.out.println("Podaj vehicleId:\n");
                    vehicleService.deleteVehicle(scanner.nextLine());
                    System.out.println("Usunięto vehicle!");
                }
                if (c == 3) {
                    vehicleService.getAllVehicles();
                }
                if (c == 4)authService.getUsers();
                if (c == 5)rentalService.getRentals();
            }else{
                System.out.println("1.Rent vehicle");
                System.out.println("2.Return vehicle");
                System.out.println("3.Show my info");
                System.out.println("4.Show all vehicles");
                int c = scanner.nextInt();
                scanner.nextLine();
                if(c == 1){
                    vehicleService.getAllVehicles();
                    System.out.println("Podaj id:");
                    System.out.println(user);
                    vehicleService.rentVehicle(scanner.nextLine(),user);
                    System.out.println("Arendowano!");
                }else if(c == 2){
                    vehicleService.returnVehicle();
                    System.out.println("Zwrocono!");
                }else if(c == 3){
                    System.out.println("Your info:");
                    System.out.println(user);
                }else if(c == 4){
                    vehicleService.getAllVehicles();
                }

            }
        }
    }

}
