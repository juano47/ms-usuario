package ms.usuario.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.usuario.domain.Usuario;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	Usuario findByUsername(String username);
		
}