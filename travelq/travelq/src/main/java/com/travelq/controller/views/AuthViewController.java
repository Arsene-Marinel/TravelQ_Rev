package com.travelq.controller.views;

import com.travelq.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class AuthViewController {

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }
}


