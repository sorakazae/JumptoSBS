package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;


import java.time.LocalDateTime;

//Pageing 구현
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.criteria.*;
//CriteriaBuilder, CriteriaQuery, Join, JoinType, Predicate, Root
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;
	
	private Specification<Question> search(String kw) {
		return new Specification<>() { 
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); //중복제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT); //Root<Question> q의 author 칼럼으로 join
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT); //Answer의 answerList로 join
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT); //answerList로 조인한 결과에 author로 join
				return cb.or(
						cb.like(q.get("subject"), "%"+kw+"%"),	//제목 like연산
						cb.like(q.get("content"), "%"+kw+"%"), //내용
						cb.like(u1.get("username"), "%"+kw+"%"), //질문 작성자
						cb.like(a.get("content"), "%"+kw+"%"), //답변 내용
						cb.like(u2.get("username"), "%"+kw+"%") //답변 작성자
						);
			}
		};	//Root<Question> q = from / CriteriaQuery<?> query = 조건 / CriteriaBuilder db = where/join절
	}
	
	public List<Question> getList(){
		return this.questionRepository.findAll();
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		}
		else {
			throw new DataNotFoundException("question not found");
		}
	}
	//질문 만들기 수행
	public void create(String subject, String contect, SiteUser user) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(contect);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		this.questionRepository.save(q);
	}
	//페이지화 해서 한 페이지에 10개씩 보여주고, 전달받은 page쪽의 결과를 반환
	public Page<Question> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);	//검색 내용이 있다면 적용
		return this.questionRepository.findAll(spec, pageable);
		//return this.questionRepository.findAllByKeyword(kw, pageable);
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}
}
