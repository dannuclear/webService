package ru.mephi3.domain;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
	@NotNull(message = "Имя не может быть пустым")
	private String name;
	@NotBlank(message = "Фамилия не может быть пустой")
	private String surname;
	@NotNull(message = "Дата рождения не может быть пусто")
	private LocalDate birthday;
}
