package ms.usuario.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import ms.usuario.domain.Usuario;
import ms.usuario.service.ClienteService;
import ms.usuario.service.impl.CustomUserDetailsService;

//se habilita la configuracion para activar un servidor de autenticacion
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private static final String CLIENT_ID = "web";
	private String CLIENT_SECRET = null;

	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String AUTHORIZATION_CODE = "authorization_code";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String SCOPE_READ = "read";
	private static final String SCOPE_WRITE = "write";
	
	@PostConstruct
	public void init(){
	    this.CLIENT_SECRET=passwordEncoder.encode("web");
	}
	
	@Value("${auth.duration.accessToken}")
	private int ACCESS_TOKEN_DURATION;
	
	@Value("${auth.duration.refreshedToken}")
	private int REFRESHED_TOKEN_DURATION;

	@Autowired
	private CustomUserDetailsService usuarioService;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	/*@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}*/

	// sobreescribimos este metodo para definir un CustomUserDetailService
	// donde esta definido el acceso a bd para buscar los datos de user
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.userDetailsService(usuarioService).authenticationManager(authManager).tokenStore(tokenStore);
	}

	// define las APLICACIONES clientes que pueden acceder al servidor de recursos
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(CLIENT_ID).secret(CLIENT_SECRET)
				.authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN)
				.scopes(SCOPE_READ, SCOPE_WRITE).accessTokenValiditySeconds(ACCESS_TOKEN_DURATION)
				.refreshTokenValiditySeconds(REFRESHED_TOKEN_DURATION);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		//ponemos como accesible la url /oauth/token
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

}
