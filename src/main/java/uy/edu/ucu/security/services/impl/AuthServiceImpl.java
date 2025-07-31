package uy.edu.ucu.security.services.impl;

import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.persistence.repositories.UserRepository;
import uy.edu.ucu.security.services.IAuthservice;           // ajustá nombre si lo tenés distinto
import uy.edu.ucu.security.services.IJWTUtilityService;
import uy.edu.ucu.security.services.models.dtos.LoginDTO;
import uy.edu.ucu.security.services.models.dtos.ResponseDTO;
import uy.edu.ucu.security.services.models.validation.UserValidation;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IJWTUtilityService jwtUtilityService;
    private final UserValidation userValidation;

    public AuthService(UserRepository userRepository,
                       IJWTUtilityService jwtUtilityService,
                       UserValidation userValidation) {
        this.userRepository     = userRepository;
        this.jwtUtilityService  = jwtUtilityService;
        this.userValidation     = userValidation;
    }

    @Override
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception {
        // ... tu implementación de login ...
    }

    @Override
    public ResponseDTO register(UserEntity user) throws Exception {
        // abrimos only ONE try here
        try {
            // 1) validaciones básicas
            ResponseDTO response = userValidation.validate(user);
            if (response.getNumOfErrors() > 0) {
                return response;
            }

            // 2) comprueba email duplicado
            List<UserEntity> all = userRepository.findAll();
            for (UserEntity existing : all) {
                if (existing.getEmail().equalsIgnoreCase(user.getEmail())) {
                    response.setMessage("User already exists!");
                    return response;
                }
            }

            // 3) encripta y guarda
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);

            response.setMessage("User created successfully!");
            return response;

        } catch (Exception e) {
            // sólo un bloque catch al final del try
            throw new Exception("Error during registration: " + e.getMessage(), e);
        }
    }

    // método auxiliar
    private boolean verifyPassword(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }
}
