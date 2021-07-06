package ru.mephi3.reposotory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import ru.mephi3.domain.ObjectClass;

@Transactional(readOnly = true)
public interface ObjectClassRepository extends JpaRepository<ObjectClass, Integer> {

//	@EntityGraph(attributePaths = { "parent" })
	List<ObjectClass> findByParentIdOrderByOkpdCode(Integer integer);
	
//	@EntityGraph(attributePaths = { "parent" })
	List<ObjectClass> findByParentIdIsNullOrderByOkpdCode();
	
	@Procedure("OBJECT_CLASS_TREE_STRING")
	String getObjectClassTreeString(Integer leafId);
}
