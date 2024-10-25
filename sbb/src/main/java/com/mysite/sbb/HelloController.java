package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller		// 스프링부트 컨트롤러임을 알려주는 애너테이션
public class HelloController {
	@GetMapping("/hello")	//GET방식 URL 요청 발생시 hello 메서드를 실행한다는 뜻(POST는 PostMapping)
	@ResponseBody	//메서드의 출력결과가 문자열 그 자체임을 나타내는 애너테이션
	public String hello() {
		return "Hello Spring Boot Board";
	}
}
