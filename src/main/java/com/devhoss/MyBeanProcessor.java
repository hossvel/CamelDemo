package com.devhoss;

import java.util.concurrent.ThreadLocalRandom;

public class MyBeanProcessor {

	
	public String Modificar(String body) {
	
		System.out.println("--------------- ");
		return body + "Hola Mundo222";
		//return new  User(body,"Hola Mundo", ThreadLocalRandom.current().nextInt());
		 
		
	}
	
}
