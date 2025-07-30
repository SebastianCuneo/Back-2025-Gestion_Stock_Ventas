package uy.edu.ucu.security.services.impl;

import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.persistence.repositories.UserRepository;
import uy.edu.ucu.security.services.impl.IAuthServiceImpl;               // Tu interfaz para el auth
import uy.edu.ucu.security.services.IJWTUtilityService;
import uy.edu.ucu.security.services.models.dtos.LoginDTO;
import uy.edu.ucu.security.services.models.dtos.ResponseDTO;
import uy.edu.ucu.security.services.models.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthServiceImpl {

    private final UserRepository        userRepository;
    private final IJWTUtilityService    jwtUtilityService;
    private final UserValidation        userValidation;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           IJWTUtilityService jwtUtilityService,
                           UserValidation userValidation) {
        this.userRepository     = userRepository;
        this.jwtUtilityService  = jwtUtilityService;
        this.userValidation     = userValidation;
    }

    @Override
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception {
        try {
            HashMap<String, String> result = new HashMap<>();

            Optional<UserEntity> optUser = userRepository.findByEmail(loginRequest.getEmail());
            if (optUser.isEmpty()) {
                result.put("error", "User not registered!");
                return result;
            }

            UserEntity user = optUser.get();
            if (verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                // genera y devuelve el JWT
                String token = jwtUtilityService.generateJWT(user.getId());
                result.put("jwt", token);
            } else {
                result.put("error", "Authentication failed");
            }
            return result;

        } catch (IllegalArgumentException e) {
            // problemas generando el token
            throw new Exception("Error generating JWT: " + e.getMessage(), e);

        } catch (Exception e) {
            // cualquier otro error
            throw new Exception("Unknown error during login", e);
        }
    }

    @Override
    public ResponseDTO register(UserEntity user) throws Exception {
        try {
            // validaciones básicas (nombres, email, etc)
            ResponseDTO response = userValidation.validate(user);
            if (response.getNumOfErrors() > 0) {
                return response;
            }

            // comprueba si ya existe alguno con ese email
            List<UserEntity> all = userRepository.findAll();
            for (UserEntity existing : all) {
                if (existing.getEmail().equalsIgnoreCase(user.getEmail())) {
                    response.setMessage("User already exists!");
                    return response;
                }
            }

            // encripta y guarda
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            response.setMessage("User created successfully!");
            return response;

        } catch (Exception e) {
            throw new Exception("Error during registration: " + e.getMessage(), e);
        }
    }

    // método auxiliar para comparar contraseñas
    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encodedPassword);
    }
}
