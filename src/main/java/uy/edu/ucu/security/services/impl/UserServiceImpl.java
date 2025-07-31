package uy.edu.ucu.security.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.persistence.repositories.UserRepository;
import uy.edu.ucu.security.services.IUserService;
import java.util.List;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }
}
