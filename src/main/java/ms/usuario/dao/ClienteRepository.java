package ms.usuario.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import frsf.isi.dan.InMemoryRepository;
import ms.usuario.domain.Cliente;

@Repository
public class ClienteRepository extends InMemoryRepository<Cliente> {
	
	@Override
	public Integer getId(Cliente entity) {
		return entity.getId();
	}
	
	@Override
	public void setId(Cliente entity, Integer id) {
		entity. setId(id);
	}
	
	public Optional<Cliente> findByCuit(String cuit) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Cliente> findByRazonSocial(Optional<String> razonSocial) {
		// TODO Auto-generated method stub
		return null;
	}
	
}