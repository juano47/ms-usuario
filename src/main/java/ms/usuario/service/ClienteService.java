package ms.usuario.service;

import java.util.List;
import java.util.Optional;

import ms.usuario.domain.Cliente;
import ms.usuario.exceptions.RiesgoException;

public interface ClienteService {

	Optional<Cliente> buscarPorId(Integer id);

	Optional<Cliente> findByCuit(String cuit);

	Optional<Cliente> findByRazonSocial(Optional<String> razonSocial);

	List<Cliente> findAll();

	Cliente save(Cliente cliente) throws RiesgoException;

	void update(Integer id, Cliente nuevo);

	void delete(Integer id);

}
