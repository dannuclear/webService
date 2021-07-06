package ru.mephi3.web.method.support;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DataTablesRequestArgumentResolver implements HandlerMethodArgumentResolver {

	public DataTablesRequestArgumentResolver() {
		super();
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return DataTablesRequest.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Optional<Integer> draw = parseAndApplyBoundaries(webRequest.getParameter("draw"), Integer.MAX_VALUE);
		Optional<Integer> start = parseAndApplyBoundaries(webRequest.getParameter("start"), Integer.MAX_VALUE);
		Optional<Integer> length = parseAndApplyBoundaries(webRequest.getParameter("length"), Integer.MAX_VALUE);

//		if (!(draw.isPresent() && start.isPresent()) && !length.isPresent()) {
//			return new DataTablesRequest(null, null, null, null);
//		}

		Iterator<String> paramIter = webRequest.getParameterNames();
		while (paramIter.hasNext()) {
			String param = paramIter.next();
			if (param.startsWith("columns")) {
				
			}
		}

//		return new DataTablesRequest(draw.get(), start.get(), length.get(), null);
		return null;
	}

	private Optional<Integer> parseAndApplyBoundaries(@Nullable String parameter, int upper) {

		if (!StringUtils.hasText(parameter)) {
			return Optional.empty();
		}

		try {
			int parsed = Integer.parseInt(parameter);
			return Optional.of(parsed < 0 ? 0 : parsed > upper ? upper : parsed);
		} catch (NumberFormatException e) {
			return Optional.of(0);
		}
	}
}
