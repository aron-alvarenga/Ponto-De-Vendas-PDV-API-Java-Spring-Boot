package com.aronalvarenga.pdv.controller;

import com.aronalvarenga.pdv.dto.ResponseDTO;
import com.aronalvarenga.pdv.entity.User;
import com.aronalvarenga.pdv.exceptions.NoItemException;
import com.aronalvarenga.pdv.repository.UserRepository;
import com.aronalvarenga.pdv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity post(@RequestBody User user) {

        try {
            user.setEnable(true);
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity put(@RequestBody User user) {
        try{
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        } catch (NoItemException e) {
            return new ResponseEntity<>(new ResponseDTO<>(e.getMessage(), user), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO<>(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {

        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
        } catch (NoItemException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO<>(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
