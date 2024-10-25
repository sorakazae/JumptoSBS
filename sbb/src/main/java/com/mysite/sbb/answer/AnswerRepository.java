package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;


public interface AnswerRepository extends JpaRepository<Answer, Integer>{
	//Page<Answer> findByQuestionId(Integer questionId, Pageable pageable);
}
