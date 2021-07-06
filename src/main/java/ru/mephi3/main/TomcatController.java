package ru.mephi3.main;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.mephi3.domain.Person;

@Controller
public class TomcatController {

	@GetMapping("/success")
	public String saySuccess(@RequestParam(value = "name", required = false) String name, Model model) {
		Person person = new Person();
		person.setName("Имя");
		person.setBirthday(LocalDate.now());
		model.addAttribute("person", person);
		return "success";
	}

	@GetMapping("/boot")
	public String getBoot(@RequestParam(value = "name", required = false) String name, Model model) {
		return "boot";
	}

	@PostMapping("/success")
	public String onSuccess(@ModelAttribute @Valid Person person, BindingResult bindingResult) {
		return "success";
	}

//	@GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<Integer> sayReact() {
//		return Flux.range(1, 100).delayElements(Duration.ofMillis(100));
//	}
}

/*
 * @RestController
 * 
 * @RequestMapping(value = "/posts") public class TomcatController {
 * 
 * @GetMapping("") public Flux<String> sayHello(){ return
 * Flux.just("123","1231"); } }
 */