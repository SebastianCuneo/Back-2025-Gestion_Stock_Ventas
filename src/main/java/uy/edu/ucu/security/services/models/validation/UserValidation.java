package uy.edu.ucu.security.services.models.validation;

import uy.edu.ucu.security.services.models.dtos.ResponseDTO;
import uy.edu.ucu.security.persistence.entities.UserEntity;

public class UserValidation {
    public ResponseDTO validate(UserEntity user){
        ResponseDTO response = new ResponseDTO();

        response.setNumOfErrors(0);

        if (user.getFirstName() == null ||
                user.getFirstName().length() < 3 ||
                user.getFirstName().length() > 15){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("the first name must be between 3 and 15 characters");

        }
        if (user.getLastName() == null ||
            user.getLastName().length() < 3 ||
            user.getLastName().length() > 30){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("the last name must be between 3 and 30 characters");
        }
        if (user.getEmail() == null ||
                !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\\\.)+[\\w-]{2,4}$\"\n")){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("the email address must be a valid email address");

        }
        if(user.getPassword() == null ||
           !user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,16}$")){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("the password must be between 8 and 16 characters and must for less have one lowercase and uppercase.");
        }

        return response;
    }

}
