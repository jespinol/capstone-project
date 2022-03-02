package com.jmel.cryonotes.controller;

import com.jmel.cryonotes.model.User;
import com.jmel.cryonotes.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class SignInUpController {

    @Autowired
    private UserRepository userRepository;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("")
    public String viewStartPage() {
        if (isAuthenticated()) {
            return "/home";
        }
        return "/login/index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/login/signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/login/signup_form";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "/login/index";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation() {
        return "redirect:/register";
    }


    @GetMapping("/home")
    public String listUsers(Model model) {
        return "/home";
    }

    @GetMapping("/edit_profile")
    public String edit(Model model, @RequestParam("id") Long id) {
        Optional<User> optional = userRepository.findById(id);
        User editItem = optional.get();
        model.addAttribute("editItem", editItem);
        return "edit_profile";
    }
}
