package com.example.demo.service;

import com.example.demo.Exceptions.UserException;

public interface MainService {
    Boolean SignUp(String email, String password) throws UserException;
    
    String Login(String email, String password) throws UserException;
}
