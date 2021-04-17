package ms.usuario.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.exceptions.RiesgoException;
import ms.usuario.service.ClienteService;
import ms.usuario.service.RiesgoCrediticioService;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Autowired
	ClienteRepository clienteRepo;

	@Autowired
	RiesgoCrediticioService riesgoService;

	@Override
	public Optional<Cliente> buscarPorId(Integer id) {
		return this. clienteRepo.findById(id);
	}
	@Override
	public Optional<Cliente> findByCuit(String cuit) {
		return clienteRepo.findByCuit(cuit);
	}
	@Override
	public Optional<Cliente> findByRazonSocial(Optional<String> razonSocial) {
		return clienteRepo.findByRazonSocial(razonSocial);
	}
	@Override
	public List<Cliente> findAll() {
		return clienteRepo.findAll();
	}
	@Override
	public Cliente save(Cliente cliente) throws RiesgoException {
		if(riesgoService.situacionBCRA(cliente.getCuit()) > 2) {
			throw new RiesgoException("BCRA");
		}
		return clienteRepo.save(cliente);
	}
	@Override
	public void update(Cliente clienteDb, Cliente nuevo) {
		clienteDb.setCuit(nuevo.getCuit());
		clienteDb.setHabilitadoOnline(nuevo.getHabilitadoOnline());
		clienteDb.setMail(nuevo.getMail());
		clienteDb.setMaxCuentaCorriente(nuevo.getMaxCuentaCorriente());
		clienteDb.setObras(nuevo.getObras());
		clienteDb.setRazonSocial(nuevo.getRazonSocial());
		clienteDb.setUser(nuevo.getUser());
		clienteRepo.save(clienteDb);

	}
	@Override
	public void delete(Integer id) {
		clienteRepo.deleteById(id);
	}
}
