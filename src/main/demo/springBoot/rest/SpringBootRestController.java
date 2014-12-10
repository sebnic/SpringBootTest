package demo.springBoot.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SpringBootRestController {

	@RequestMapping(value="/")
	public String hello() {
		return "Hello";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestController.class, args);
	}
}
