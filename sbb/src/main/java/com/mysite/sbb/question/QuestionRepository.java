package com.mysite.sbb.question;

import java.util.List;

//Paging 구현을 위한 클래스
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; //검색기능 구현
import org.springframework.data.jpa.repository.JpaRepository;
//JpaRepository는 JPA가 제공하는 인터페이스 중 하나로 CRUD작업을 처리하는 메서드들을 내장

//SQL문을 직접 작성하여 Query 하기
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mysite.sbb.answer.Answer;


public interface QuestionRepository extends JpaRepository<Question, Integer> {
	Question findBySubject(String subject);	//칼럼을 이용해 값을 조회하기 위해 미리 선언
	Question findBySubjectAndContent(String subject, String content); //where에 And 조건으로 조회
	List<Question> findBySubjectLike(String subject);	// 리턴 값이 1개가 아닐 경우 List로 받아야
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	//SQL문 작성시 엔티티 기준으로 작성해야 함
	@Query("select "
			+ "distinct q "
			+ "from Question q "
			+ "left outer join SiteUser u1 on q.author=u1 "
			+ "left outer join Answer a on a.question=q "
			+ "left outer join SiteUser u2 on a.author=u2 "
			+ "where "
			+ "q.subject like %:kw% "
			+ "or q.content like %:kw% "
			+ "or u1.username like %:kw% "
			+ "or a.content like %:kw% "
			+ "or u2.username like %:kw% "
			)
	Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
