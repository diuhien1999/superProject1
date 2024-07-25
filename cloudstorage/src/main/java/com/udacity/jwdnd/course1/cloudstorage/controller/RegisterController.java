package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.UserModel;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String register() {
        return "signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute UserModel user, Model model, RedirectAttributes redirectAttributes) {
        String status = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            status = "Username already registered, please choose another one";
        }

        if (status == null) {
            int rowsAdded = userService.registerUser(user);

            if (rowsAdded < 0) {
                status = "Registration failed, please try again.";
            }
        }

        if (status == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        }

        model.addAttribute("signupError", status);
        return "signup";
    }
}
