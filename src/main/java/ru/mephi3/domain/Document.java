package ru.mephi3.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DOCUMENT")
public class Document {

	@Id
	@Column(name = "ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "HEADER")
	private String header;
	@Column(name = "TITLE")
	private String title;
	@Column(name = "COMMIT_DATE")
	private LocalDate commitDate;
	@Column(name = "BEGIN_DATE")
	private LocalDate beginDate;
	@Column(name = "END_DATE")
	private LocalDate endDate;
	@Column(name = "STATUS")
	private Status status;
	@Column(name = "COMMENT")
	private String comment;

	public static enum Status {
		ACTIVE("Действующий"), REJECT("Отменен");

		private String label;

		private Status(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}
