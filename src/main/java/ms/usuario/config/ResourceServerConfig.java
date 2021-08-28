package ms.usuario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

//se habilita la configuracion para activar un servidor de recursos
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		  http.cors().and()
		  .authorizeRequests()
		  //permitimos las url /oauth
		  .antMatchers("/oauth/**","/swagger-ui.html","/swagger/**","/springfox-swagger-ui/**",
				  "/swagger-resources/**","/api/v2/**","/v2/**").permitAll()
		  //definimos seguridad para cualquier request /api/**
		  .antMatchers("/api/**").authenticated()
		  .anyRequest().authenticated()
		  //devolver 403 si no tiene privilegios de acceso
		  .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
		  .and().httpBasic();
	
	}

}