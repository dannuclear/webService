package ru.mephi3.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("private/sessions")
public class SessionController {
	private final SessionRegistry sessionRegistry;

	@GetMapping
	public String findAll(Model model) {
		List<SessionInformation> data = new ArrayList<>();
		for (Object principal : sessionRegistry.getAllPrincipals())
			for (SessionInformation si : sessionRegistry.getAllSessions(principal, false))
				data.add(si);
		model.addAttribute("sessions", data);
		return "private/sessions";
	}
}
