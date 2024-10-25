package com.mysite.sbb;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration	//스프링 시큐리티 설정파일
@EnableWebSecurity	//스프링 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled = true)	//@PreAuthorize 사용 활성화
public class SecurityConfig {	
	@Bean	//스프링에 의해 생성/관리되는 객체(컨트롤러, 서비스, 리포지토리 등도 빈임)
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(		//로그인을 구현하려니 게시물 자체를 볼 수 없게 되어 루트경로 접근 허용
				(authorizeHttpRequests) ->
				authorizeHttpRequests.requestMatchers(
						new AntPathRequestMatcher(
								"/**"
						)
				).permitAll()
		).csrf(
				(csrf) ->	//H2-console은 스프링 프레임워크가 아니기에 csrf의 적용을 받지않음(예외처리) 
				csrf.ignoringRequestMatchers(
						new AntPathRequestMatcher(
								"/h2-console/**"
						)
				)
		).headers(		//XFrameOptionHeader가 기본적으로 DENY라서 H2-console의 FRAME뷰가 정상적으로 뜨지 않음
				(headers) -> 
				headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(
								XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
						)
				)
		).formLogin(
				(formLogin) ->
				formLogin.loginPage(
						"/user/login"
				).defaultSuccessUrl(
						"/"
				)
				
		).logout(
				(logout) ->
				logout.logoutRequestMatcher(
						new AntPathRequestMatcher(
								"/user/logout"
						)
				).logoutSuccessUrl(
						"/"
				).invalidateHttpSession(true)	//사용자 세션 삭제
		);
		return http.build();
	}
	
	@Bean	//암호 해시화
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean	// 스프링 시큐리티 인증처리
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
}

