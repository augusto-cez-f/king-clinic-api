package com.kingtest.services.UserDataService;

import org.springframework.stereotype.Service;

import com.kingtest.entities.User;
import com.kingtest.services.MedicosService;
import com.kingtest.services.RecepcionistaService;

@Service
public class UserDataServiceFactory {

    private final MedicosService medicosService;
    private final RecepcionistaService recepcionistaService;
    // Add repositories for other roles as needed

    public UserDataServiceFactory(MedicosService medicosService, RecepcionistaService recepcionistaService) {
        this.medicosService = medicosService;
        this.recepcionistaService = recepcionistaService;
        // Initialize other repositories here
    }

    public UserDataService getUserDataService(User user) {
        String role = user.getRole();

        switch (role) {
            case "M":
                return medicosService;
            case "R":
                return recepcionistaService;
            // Add cases for other roles and repositories as needed
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
}
