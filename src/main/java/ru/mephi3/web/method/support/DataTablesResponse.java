package ru.mephi3.web.method.support;

import java.util.Collection;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataTablesResponse<T> {
	private Integer draw;
	private Long recordsTotal;
	private Integer recordsFiltered;
	private Collection<T> data;
	private String error;
	private String DT_RowId;
	private String DT_RowClass;
	private T DT_RowData;
	private T DT_RowAttr;

	public static <V> DataTablesResponse<V> of(Integer draw, Page<V> page) {
		return DataTablesResponse.of(draw, page.getTotalElements(), page.getNumberOfElements(), page.getContent());
	}

	public static <V> DataTablesResponse<V> of(Integer draw, Long recordsTotal, Integer recordsFiltered,
			Collection<V> data) {
		return new DataTablesResponse<V>(draw, recordsTotal, recordsTotal.intValue(), data, null, null, null, null, null);
	}
	
	public static <V> DataTablesResponse<V> of(Integer draw, String error) {
		return new DataTablesResponse<V>(draw, null, null, null, error, null, null, null, null);
	}
}
