package com.example.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.AuthRequestDTO;
import com.example.demo.DTO.AuthResponseDTO;
import com.example.demo.DTO.RequestStatus;
import com.example.demo.Exceptions.UserException;
import com.example.demo.service.AuthSerivce;
import com.example.demo.service.MainService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    MainService authSerivce;

    public AuthController(AuthSerivce authSerivce){
        this.authSerivce = authSerivce;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> SignUp(@RequestBody AuthRequestDTO authRequestDTO){

        AuthResponseDTO responseDTO = new AuthResponseDTO();

        try {
            if(authSerivce.SignUp(authRequestDTO.getEmail(), authRequestDTO.getPassword())){
                responseDTO.setRequestStatus(RequestStatus.SUCCESS);
            }
            else{
                responseDTO.setRequestStatus(RequestStatus.FAILURE);
            }
        } catch (UserException e) {
            responseDTO.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseDTO,HttpStatus.ACCEPTED);

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> Login(@RequestBody AuthRequestDTO authRequestDTO){
        String token;
        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setRequestStatus(RequestStatus.SUCCESS);
        try {
            token = authSerivce.Login(authRequestDTO.getEmail(), authRequestDTO.getPassword());
        } catch (UserException e) {
            responseDTO.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
        }

        

        MultiValueMap<String,String> mValueMap = new LinkedMultiValueMap<>();

        mValueMap.add("AUTH_TOKEN", token);

        return new ResponseEntity<>(
            responseDTO,mValueMap,HttpStatus.OK );
        

    }

    

}
