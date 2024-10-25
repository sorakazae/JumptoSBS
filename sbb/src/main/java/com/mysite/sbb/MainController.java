package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/sbb")
	@ResponseBody		//ResponseBody 애너테이션을 빼먹으면 index라는 이름의 템플릿 파일을 찾음
	public String index() {
		return "안녕하세요 sbb에 오신 것을 환영합니다.";
		//System.out.println("index");
	}
	
	@GetMapping("/")	//root 도메인 접속시 리다이렉션
	public String root() {
		return "redirect:/question/list";
	}
}
