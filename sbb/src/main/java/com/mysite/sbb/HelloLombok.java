package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;	//final로 선언한 속성을 필요로하는 생성자를 자동으로 만들어줌
// import lombok.Setter;

@Getter
@RequiredArgsConstructor
//@Setter	//final을 쓰게 되면 속성을 변경할 수 없기 때문에 Setter을 사용할 수 없다
public class HelloLombok {
	// private String hello;
	// private int lombok;
	private final String hello;
	private final int lombok;

	/*		lombok을 사용하지 않으면 아래와 같이 생성자를 따로 만들어서 사용해야 하기 때문에 편리함을 위해 사용한다


	public class HelloLombok(String hello, int lombok) {
		this.hello = hello;
		this.lombok = lombok;
	}
	*/
	
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok("헬로",5);
//		helloLombok.setHello("헬로");		// lombok을 사용하면 메서드를 선언해주지 않아도 알아서 만들어줌
//		helloLombok.setLombok(5);
		
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
	}
}

// 즉 lombok은 생성자를 자동으로 만들어준다