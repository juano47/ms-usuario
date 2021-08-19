package ms.usuario.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.dao.UsuarioRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Usuario;

//este servicio es usado por el servidor de autenticacion para obtener los datos
//de user desde bd

//contiene la información del usuario autenticado 
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	UsuarioRepository usuarioRepository;
	
	//@Autowired
	//UserMsRepository userMsRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = null;
//		UserMs userMs;
//		if(username.equals("GESTOR_IMPRESION")) {
//			userMs = userMsRepository.findByUsername(username);
//			if (userMs == null) {
//				throw new UsernameNotFoundException(String.format("User '%s' not found", username));
//			} 
//			return userMs;
//		}else {
			usuario = usuarioRepository.findByUsername(username);
			if (usuario == null) {
				throw new UsernameNotFoundException(String.format("User '%s' not found", username));
			}
			return usuario;
		//}
	}

//	public User getUserByUsername(String username) {
//		User user = userRepository.findByUsername(username);
//		if (user!=null) {
//			return user;
//		} else {
//			return null;
//		}
//	}

}