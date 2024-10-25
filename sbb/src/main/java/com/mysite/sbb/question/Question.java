package com.mysite.sbb.question;

import java.util.Set;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import com.mysite.sbb.user.SiteUser;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.sbb.answer.Answer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter		//DB 엔티티 생성시 Setter을 사용하면 보안문제가 발생, 생성자, 메서드를 이용하여 접근하게
@Entity
public class Question {
	@Id //Primary Key 지정하는 애너테이션
	@GeneratedValue(strategy = GenerationType.IDENTITY) //고유값(인덱스) 부여(자동으로 1씩 증가)
	private Integer Id;
	
	@Column(length = 200) // 길이가 200인 칼럼
	private String subject;
	
	@Column(columnDefinition = "TEXT") // 데이터 형식이 "TEXT"인 칼럼
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)	//List로 들어온 답변들을 question 칼럼으로 연결되되, 지워지면 같이 지워지게 함
	private List<Answer> answerList;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	@ManyToMany
	Set<SiteUser> voter;
}
