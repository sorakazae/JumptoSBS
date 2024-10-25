package com.mysite.sbb.answer;

import java.util.Set;
import jakarta.persistence.ManyToMany;

import com.mysite.sbb.user.SiteUser;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;	//package화로 인해 도메인이 변경되어 추가됨

import jakarta.persistence.Column;
import jakarta.persistence.Entity;	
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne	// 1:N관계 형성 Foreign Key
	private Question question;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	@ManyToMany
	Set<SiteUser> voter;
}
