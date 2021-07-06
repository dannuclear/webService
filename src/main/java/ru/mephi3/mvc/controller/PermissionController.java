package ru.mephi3.mvc.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Permission;
import ru.mephi3.service.PermissionService;

@Controller
@RequestMapping("/private/permissions")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping
	public String findAll(Pageable pageable, Model model) {
		model.addAttribute("permissions", permissionService.findAll(pageable));
		return "private/permissions";
	}

	@GetMapping("{permissionId}")
	public String findById(@PathVariable("permissionId") Integer permissionId, Model model) {
		Permission permission = permissionService.findById(permissionId);
		if (permission == null)
//			permission = Permission.builder().isActive(false).isSuperpermission(false).build();
		model.addAttribute("permission", permission);
		return "private/permission";
	}

	@PostMapping("{permissionId}")
	public String save(@ModelAttribute @Valid Permission permission, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "private/permission";
		}
		permissionService.save(permission);
		return "redirect:/private/permissions";
	}

	@GetMapping(path = "{permissionId}", params = { "delete" })
	public String delete(@PathVariable("permissionId") Permission permission) {
		permissionService.delete(permission);
		return "redirect:/private/permissions";
	}
}