package fr.ans.api.sign.esignsante.psc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfiguration.
 */
@Profile(value = {"swagger"})
@PropertySource("classpath:swagger.properties")
@Configuration
//@EnableSwagger2
public class SwaggerConfiguration  {

//public class SwaggerConfiguration extends WebMvcConfigurerAdapter {                                    
//CJU
//	@Bean
//	    public Docket api() { 
//	        return new Docket(DocumentationType.SWAGGER_2)  
//	          .select()                                  
//	          .apis(RequestHandlerSelectors.any())              
//	          .paths(PathSelectors.any())                          
//	          .build();                                           
//	    }
//
//	   @Override
//	   public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	       registry.addResourceHandler("swagger-ui.html")
//	       .addResourceLocations("classpath:/META-INF/resources/");
//CJU
}
