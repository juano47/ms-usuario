package ms.usuario.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
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
		Optional<Cliente> c =  this.findAll()
				.stream()
				.filter(unCli -> unCli.getCuit().equals(cuit))
				.findFirst();
		return c;
	}

	public Optional<Cliente> findByRazonSocial(Optional<String> razonSocial) {
		Optional<Cliente> c =  this.findAll()
				.stream()
				.filter(unCli -> unCli.getRazonSocial().equals(razonSocial.get()))
				.findFirst();
		return c;
	}

	public List<Cliente> findAll(){
		return StreamSupport.stream(super.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}

}