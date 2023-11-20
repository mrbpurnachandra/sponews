package com.mrbpurnachandra.sponews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class SponewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SponewsApplication.class, args);
	}

	@GetMapping
	public String home() {
		return "home"; 
	}

}
