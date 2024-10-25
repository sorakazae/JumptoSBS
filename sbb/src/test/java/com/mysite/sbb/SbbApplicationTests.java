package com.mysite.sbb;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

//조회 결과 값이 여러건인 경우 Question 타입이 아닌 List<Question>으로 받아야 한다
//import java.util.List;	//모든 Question을 List로 받아오기 위해 사용
//import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//import org.springframework.transaction.annotation.Transactional;

//import com.mysite.sbb.answer.Answer;
//import com.mysite.sbb.answer.AnswerRepository;
//import com.mysite.sbb.question.Question;
//import com.mysite.sbb.question.QuestionRepository;

import com.mysite.sbb.question.QuestionService;

@SpringBootTest	//스프링 부트의 테스트 클래스임을 의미
class SbbApplicationTests {

//	@Autowired	//의존성 주입(DI), 스프링이 객체를 대신 생성하여 주입
//	private QuestionRepository questionRepository; 
	@Autowired
	private QuestionService questionService;
//	@Autowired
//	private AnswerRepository answerRepository;
	
//	@Transactional	//TEST코드 실행 중에 DB세션이 종료되지 않게 해주는 에너테이션
	@Test
	void testJpa() {
		// 테스트 값 넣기
		for (int i=1; i<=300;i++) {
			String subject = String.format("테스트 데이터입니다[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject,content,null);
		}
		
		/*
		Optional<Question> oq = this.questionRepository.findById(2);	//조회 후 DB세션 종료(테스트에서만)
		assertTrue(oq.isPresent());
		Question q = oq.get();	// q객체를 조회할 때 가져오면 Eager(즉시) 방식
		
		List<Answer> answerList = q.getAnswerList();	//DB세션이 종료되어 오류 발생(테스트에서만) Lazy(지연)방식
		
		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
		*/
	/*	//findById와 getId를 활용하여 질문과 매칭되는 답변 조회하기
	 	Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());	//get+테이블명
	/*
	/*	//Answer 테이블에 답변 저장
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");	//set+칼럼명()
		a.setQuestion(q);	// 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다 (@ManyToOne을 이용한 FK 지정)
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	*/
	/*	//데이터 삭제
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	*/
	/*	//데이터 수정
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());	//oq.isPresent()가 참이면 진행, 거짓이면 오류(실패)
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	*/
	/*
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	*/
	/*	//And 조건을 이용하여 칼럼 조회
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	*/
	/*
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());	//findBySubject를 선언만 했는데도 JPA리포지터리가 메서드 이름을 기반으로 자동 기능구현
		//findBy + 칼럼명(); = JPA가 메서드 자동 생성해줌
	*/	
	/*	//@Id 값을 통해 조회, 값이 null일 경우 (예외)처리법
		Optional<Question> oq = this.questionRepository.findById(1);	//Optional은 null을 받을 수 있는 형식으로 조회한 값이 존재하지 않는 경우를 대비하여 사용
		if(oq.isPresent()) {	//oq가 null이 아니면(Id가 1인 값이 존재 하면)
			Question q = oq.get();	//Question 형식 q에 oq로 받은 조회값을 받음
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	*/
	/*	//question 전체 내용 조회(findAll()을 이용한 select * from question; 기능 구현
		List<Question> all = this.questionRepository.findAll();		//findAll 메서드를 이용하여 테이블 내 전체 내용 조회 기능 구현
		assertEquals(2, all.size());		// 받아온 값이 기대값과 일치하는지 검사하는 구문(틀리면 실패)
		
		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	*/
	/*	//테이블에 내용(질문,답변)을 저장함
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1); // 첫번째 질문 저장
		
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2); // 두번째 질문 
	 */
	}

}
