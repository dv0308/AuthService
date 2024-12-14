package com.example.demo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.Role;
import com.example.demo.models.User;

import io.jsonwebtoken.Jwts;

import com.example.demo.Exceptions.UserException;
import com.example.demo.Repositories.UserRepo;

@Service
public class AuthSerivce implements MainService{

    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    SecretKey key = Jwts.SIG.HS256.key().build();


    public AuthSerivce(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public Boolean SignUp(String email, String password) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            throw new UserException("User Already exists!!!");
        }

        User nUser = new User();
        nUser.setEmail(email);
        nUser.setPassword(bCryptPasswordEncoder.encode(password));

        userRepo.save(nUser);
        return true;
    }

    @Override
    public String Login(String email, String password) throws UserException{
        Optional<User> nUser = userRepo.findByEmail(email);
        if(!nUser.isPresent()){
            throw new UserException("User Not exists!!!");
        }

        if(!bCryptPasswordEncoder.matches(password, nUser.get().getPassword())){
            throw new UserException("Wrong Password!!!");
        }
        return getJwtToken(email, new ArrayList<>(), nUser.get().getId());
    }

     private String getJwtToken(String email , List<Role> roles, Long userId){

        Map<String,Object> claims = new HashMap<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 30);
        date = calendar.getTime();

        claims.put("user_id",userId);
        claims.put("email",email);
        claims.put("roles",roles);

        String jwt = Jwts.builder().
        claims(claims)
        .issuer("divyansh.dev")
        .expiration(date)
        .issuedAt(new Date())
        .signWith(key)
        .compact();
        
        return jwt;
     }
    
}
