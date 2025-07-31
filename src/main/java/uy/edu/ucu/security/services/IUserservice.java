package uy.edu.ucu.security.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uy.edu.ucu.security.persistence.entities.UserEntity;

import java.util.List;
@RestController
@RequestMapping

public interface IUserservice {
    @Autowired
    IUserservice userService;
    @GetMapping("/all")
    private ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

}
