package fr.ans.api.sign.esignsante.psc.config;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.models.Contact;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.builders.PathSelectors;

/*
 * WebConfig
 */
@Configuration
//@Import(SwaggerConfiguration.class)
@EnableSwagger2 // CJU

public class WebConfig {// {implements WebMvcConfigurer {

//@Value("${application.version}")
//private String version;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #configureMessageConverters(java.util.List)
	 */
	/*
	 * @Override public void configureMessageConverters(final
	 * List<HttpMessageConverter<?>> converters) { converters.add(new
	 * MappingJackson2HttpMessageConverter()); }
	 */
	// CJU
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false) // Ne pas avoir les erreurs par defaut 401-403-404
				.select()
				.apis(RequestHandlerSelectors
				.basePackage("fr.ans.api.sign.esignsante.psc.api")) // apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

//    private ApiInfo apiInfo() {
//        return new ApiInfo("Langton ant app", "rest api for langton ant app", version, null,
//                new Contact(), null, null, Collections.EMPTY_LIST);
//    }
//    
	// FIN CJU

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addResourceHandlers(org.springframework.web.servlet.config.annotation.
	 * ResourceHandlerRegistry)
	 */
	/*
	 * @Override public void addResourceHandlers(final ResourceHandlerRegistry
	 * registry) {
	 * registry.addResourceHandler("swagger-ui.html").addResourceLocations(
	 * "classpath:/META-INF/resources/");
	 * 
	 * registry.addResourceHandler("/webjars/**").addResourceLocations(
	 * "classpath:/META-INF/resources/webjars/"); }
	 */
}
