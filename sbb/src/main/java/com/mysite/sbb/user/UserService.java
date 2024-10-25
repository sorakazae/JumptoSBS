package com.mysite.sbb.user;

import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;	//Bean으로 주입받아 사용 가능
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username); //lombok의 Setter로 자동 생성
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.userRepository.save(user);
		return user;
	}
	
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteuser not found");
		}
	}

}
