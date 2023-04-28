package com.example.finalproject.util;

import com.example.finalproject.models.User;
import com.example.finalproject.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.getUserByLogin(user) != null){
            errors.rejectValue("login", "",
                    "Данный логин уже зарегистрирован");
        }
    }
}

