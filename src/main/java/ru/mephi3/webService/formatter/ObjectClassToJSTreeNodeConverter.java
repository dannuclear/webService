package ru.mephi3.webService.formatter;

import org.springframework.core.convert.converter.Converter;

import ru.mephi3.domain.ObjectClass;
import ru.mephi3.dto.JSTreeNode;

public class ObjectClassToJSTreeNodeConverter implements Converter<ObjectClass, JSTreeNode> {

	@Override
	public JSTreeNode convert(ObjectClass source) {
		JSTreeNode jsTreeNode = new JSTreeNode();
		jsTreeNode.setId(source.getId().toString());
		jsTreeNode.setText(createName(source.getOkpdCode(), source.getName()));
		jsTreeNode.setChildren(true);
//		if (!CollectionUtils.isEmpty(source.getChilds()))
//			jsTreeNode.setChildren(source.getChilds().stream().map(this::convert).collect(Collectors.toList()));
		return jsTreeNode;
	}

	private String createName(String code, String name) {
		if (code != null && code.length() > 0)
			return code + ": " + name;
		return name;
	}
}
