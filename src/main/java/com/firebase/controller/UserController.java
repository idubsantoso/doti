package com.firebase.controller;

import com.firebase.entity.Users;
import com.firebase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/{email}")
    public Users getUser(@PathVariable String email) throws ExecutionException, InterruptedException{
        return service.getUsers(email);
    }

    @GetMapping("")
    public List<Users> getAllUsers() throws ExecutionException, InterruptedException, ParseException {
        return service.getListUsers();
    }

}
