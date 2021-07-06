
package ru.mephi3.mvc.controller;

import java.security.Principal;
import java.util.Collection;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.RequiredArgsConstructor;
import ru.mephi3.domain.Permission;
import ru.mephi3.domain.Role;
import ru.mephi3.domain.User;
import ru.mephi3.mvc.utils.ModelUtils;
import ru.mephi3.service.PermissionService;
import ru.mephi3.service.RoleService;
import ru.mephi3.service.UserService;

@Controller
@RequestMapping("/private/users")
@RequiredArgsConstructor
@SessionAttributes({"user", "permissions", "roles"})
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "private/users";
    }

    @GetMapping("{userId}")
    @Transactional(readOnly = true)
    public String edit(@PathVariable("userId") Integer userId, Model model) {
        User user = userService.findById(userId).orElse(User.defaultUser());
        if (user != null) {
            Hibernate.initialize(user.getRoles());
            Hibernate.initialize(user.getPermissions());
        }
        ModelUtils.addIfNotExist(model, "user", user);
        ModelUtils.addIfNotExist(model, "permissions", permissionService.findAll(PageRequest.of(0, 9999999)).getContent());
        ModelUtils.addIfNotExist(model, "roles", roleService.findAll(PageRequest.of(0, 9999999)).getContent());

        return "private/user";
    }

    @PostMapping("{userId}")
    public String save(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return "private/user";
        }
        if (user.getIsSuperuser())
            user.setIsActive(true);
        userService.save(user);
        sessionStatus.setComplete();
        return "redirect:/private/users";
    }

    @GetMapping(path = "{userId}", params = {"delete"})
    public String delete(@PathVariable("userId") User user) {
        userService.delete(user);
        return "redirect:/private/users";
    }

    @PostMapping(path = "{userId}", params = {"addPermission"})
    public String permissionAdd(@ModelAttribute("user") User user, Model model, @ModelAttribute("permissionId") Integer permissionId, @ModelAttribute(name = "permissions", binding = false) Collection<Permission> permissions) {
        Permission permission = permissions.stream().filter(p -> permissionId.equals(p.getId())).findFirst().orElse(null);
        user.getPermissions().add(permission);
        return "private/user";
    }

    @PostMapping(path = "{userId}", params = {"addRole"})
    public String roleAdd(@ModelAttribute("user") User user, Model model, @ModelAttribute("roleId") Integer roleId, @ModelAttribute(name = "roles", binding = false) Collection<Role> roles) {
        Role role = roles.stream().filter(p -> roleId.equals(p.getId())).findFirst().orElse(null);
        user.getRoles().add(role);
        return "private/user";
    }

    @GetMapping(path = "{userId}/permissions/{permissionId}/delete")
    public String deletePermission(@ModelAttribute("user") User user, @PathVariable("permissionId") Integer permissionId) {
        user.getPermissions().removeIf(p -> permissionId.equals(p.getId()));
        return "redirect:/private/users/{userId}";
    }

    @GetMapping(path = "{userId}/roles/{roleId}/delete")
    public String deleteRole(@ModelAttribute("user") User user, @PathVariable("roleId") Integer roleId) {
        user.getRoles().removeIf(p -> roleId.equals(p.getId()));
        return "redirect:/private/users/{userId}";
    }
}