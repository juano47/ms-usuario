package ms.usuario.service;

import java.util.Optional;

import ms.usuario.domain.Cliente;

public interface ClienteService {

	Optional<Cliente> buscarPorId(Integer id);

}
