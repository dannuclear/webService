package ru.mephi3.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.mephi3.domain.ObjectClass;
import ru.mephi3.dto.JSTreeNode;
import ru.mephi3.dto.ObjectClassDTO;
import ru.mephi3.service.ObjectClassService;
import ru.mephi3.service.exception.ObjectClassNotFoundException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/private/objectClasses")
@RequiredArgsConstructor
@SessionAttributes({"objectClassProperty", "documentsByTypeMap", "documentTypes"})
public class ObjectClassController {
    public static final String EDIT_VIEW = "/private/objectClass";
    public static final String ALL_VIEW = "/private/objectClasses";

    private final ObjectClassService objectClassService;
    private final ConversionService conversionService;

    @GetMapping("/jsTree")
    @ResponseBody
    public ResponseEntity<List<JSTreeNode>> findByParentId(@RequestParam("id") String parentId) {
        List<ObjectClass> data = null;
        if (parentId == null || "#".equals(parentId))
            data = objectClassService.findByParent(null);
        else {
            Integer pid = Integer.valueOf(parentId);
            data = objectClassService.findByParent(pid);
        }
        List<JSTreeNode> nodes = data.stream().map(oc -> conversionService.convert(oc, JSTreeNode.class)).collect(Collectors.toList());
        return ResponseEntity.ok(nodes);
    }

    @GetMapping
    public String findAll(Principal principal, Authentication authentication, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return ALL_VIEW;
    }

    @GetMapping("{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ObjectClassDTO objectClass = objectClassService.findById(id).map(oc -> {
            return ObjectClassDTO.fromDomain(oc);
        }).orElseThrow(() -> new ObjectClassNotFoundException(id));

        model.addAttribute("objectClass", objectClass);
        return EDIT_VIEW;
    }

    @GetMapping("/new")
    public String create(Model model) {
        ObjectClassDTO objectClassDTO = ObjectClassDTO.createDefault();
        model.addAttribute("objectClass", objectClassDTO);
        return EDIT_VIEW;
    }

    @PostMapping("{id}")
    public String save(@ModelAttribute("objectClass") @Valid ObjectClass objectClass, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW;
        }
        objectClassService.save(objectClass);
        sessionStatus.setComplete();
        return "redirect:" + ALL_VIEW;
    }

    @GetMapping(path = "delete/{id}")
    public String delete(@PathVariable("id") Integer id, ModelAndView mv) {
        objectClassService.findById(id).map(oc -> {
            objectClassService.delete(oc);
            return oc;
        }).orElseThrow(() -> new ObjectClassNotFoundException(id));

        mv.setStatus(HttpStatus.NO_CONTENT);
        return "redirect:" + ALL_VIEW;
    }
}