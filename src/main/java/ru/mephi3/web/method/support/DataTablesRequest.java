package ru.mephi3.web.method.support;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataTablesRequest {
	private Integer draw;
	private Integer start;
	private Integer length;
	private List<Column> columns;
	private List<Order> order;
	private Search search;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Column {
		private String data;
		private String name;
		private Boolean searchable;
		private Boolean orderable;
		private Search search;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Order {
		private Integer column;
		private String dir;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Search {
		private String value;
		private Boolean regex;
	}

	public Pageable getPageRequest() {
		Sort sort = null;
		if (order != null && order.size() > 0) {
			List<Sort.Order> orders = order.stream().map(o -> "desc".equals(o.getDir()) ? Sort.Order.desc(columns.get(o.getColumn()).getData()) : Sort.Order.asc(columns.get(o.getColumn()).getData())).collect(Collectors.toList());
			sort = Sort.by(orders);
		}
		if (sort != null)
			return PageRequest.of(start/length, length, sort);
		return PageRequest.of(start, length);
	}
}
