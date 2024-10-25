package com.mysite.sbb.question;

import java.security.Principal;
import com.mysite.sbb.user.*;	//SiteUser, UserService

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;	//template로 전달하기 위한 클래스
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.access.prepost.PreAuthorize;

import com.mysite.sbb.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



//import org.springframework.web.bind.annotation.ResponseBody; //template 없이 사용할 때

@RequestMapping("/question")	//url 프리픽스(기본 경로 설정) 선택사항
@RequiredArgsConstructor	//lombok애너테이션 final 속성을 포함하는 생성자 자동 생성
@Controller
public class QuestionController {
	
	//private final QuestionRepository questionRepsitory;
	//Repository를 이용해 엔티티를 건드는 것은 보안상 좋지 않음
	//서비스를 이용하여 접근하게 만들어야 함
	private final QuestionService questionService;
	//컨트롤러 -> 서비스 -> 리포지토리
	private final UserService userService;
	
	@GetMapping("/list")	//RequestMapping을 이용하여 /question 생략
	//@GetMapping("/question/list")
	//@ResponseBody
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value = "kw", defaultValue = "" ) String kw) {	
		//List<Question> questionList = this.questionRepsitory.findAll();
		
		//List<Question> questionList = this.questionService.getList();
		//model.addAttribute("questionList", questionList); //Model 객체가 class와 template를 연결해줌
		
		Page<Question> paging = this.questionService.getList(page, kw);
		model.addAttribute("paging", paging);	//페이지 유지
		model.addAttribute("kw", kw);	//검색창에 검색어 유지
		return "question_list";
	}
	/*	//thymeleaf 사용 전 코드
	public String list() {
		return "question_list";	//ResponseBody 없이 작성하게 되면 template의 question_list.html을 찾게 됨
	}*/
	
	@GetMapping(value = "/detail/{id}")
	//@GetMapping(value = "/question/detail/{id}")	//@PathVariable()는 숫자 2 같이 변하는 id값을 얻을 때 사용->URL에 반영하기 위함
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")	//로그인 한 사용자만 접근허용(아니면 로그인 페이지로 보내버림)
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")	//매개변수의 형태가 다르기에 questionCreate를 동일한 이름으로 사용 가능(메서드 오버로딩)
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {	//@Valid를 사용해야 QuestionForm에서 지정한 제약조건이 먹음
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list";
	}
	/*
	public String questionCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
		this.questionService.create(subject, content);
		return "redirect:/question/list"; //질문 저장 후 질문 목록으로 이동
	}*/
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
			Principal principal, @PathVariable("id") Integer id) {
		if(bindingResult.hasErrors()) {
			return "question_form";
		}
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); 
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.questionService.delete(question);
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
}
