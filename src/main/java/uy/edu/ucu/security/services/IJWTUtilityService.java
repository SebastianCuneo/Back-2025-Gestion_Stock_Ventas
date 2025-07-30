package uy.edu.ucu.security.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public interface IJWTUtilityService {
    public String generateJWT(Long user_id) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;
    public JWTClaimsSet parseJWT(String jwt) throws JOSEException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException;
}


