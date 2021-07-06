package ru.mephi3.mvc.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/private")
public class MainController {

	@GetMapping("/main")
	public String main(Principal principal, Authentication authentication) {
		return "private/main";
	}
}