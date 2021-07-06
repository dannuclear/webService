package ru.mephi3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JSTreeNode {
	private String id;
	private String text;
	private String icon;
	private State state;
	private Boolean children;

	@Getter
	@Setter
	@NoArgsConstructor
	private static class State {
		private Boolean opened;
		private Boolean disabled;
		private Boolean selected;
	}
}
