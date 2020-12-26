package com.thyng;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class ThyngWebApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
		.bannerMode(Mode.OFF)
		.web(WebApplicationType.SERVLET)
		.sources(ThyngWebApplication.class)
		.profiles("jet-member", "dev")
		.build(args)
		.run(args);
	}
	
	 @Bean  
     public InternalResourceViewResolver jspViewResolver() {  
         InternalResourceViewResolver resolver= new InternalResourceViewResolver();  
         resolver.setPrefix("/views/");  
         resolver.setSuffix(".jsp");  
         return resolver;  
     }   

}
