package ms.usuario.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.exceptions.RiesgoException;
import ms.usuario.service.ClienteService;
import ms.usuario.service.PedidoService;
import ms.usuario.service.RiesgoCrediticioService;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Autowired
	ClienteRepository clienteRepo;

	@Autowired
	RiesgoCrediticioService riesgoService;

	@Autowired
	PedidoService pedidoService;

	@Override
	public Optional<Cliente> buscarPorId(Integer id) {
		Optional<Cliente> cliente = this.clienteRepo.findById(id);
		if(cliente.isPresent() && cliente.get().getFechaBaja() == null)
			return cliente;

		return Optional.empty();
	}

	@Override
	public Optional<Cliente> findByCuit(String cuit) {
		Optional<Cliente> cliente = this.clienteRepo.findByCuit(cuit);

		if(cliente.isPresent() && cliente.get().getFechaBaja() == null)
			return cliente;

		return Optional.empty();
	}

	@Override
	public Optional<Cliente> findByRazonSocial(Optional<String> razonSocial) {
		Optional<Cliente> cliente = this.clienteRepo.findByRazonSocial(razonSocial);

		if(cliente.isPresent() && cliente.get().getFechaBaja() == null)
			return cliente;

		return Optional.empty();
	}

	@Override
	public List<Cliente> findAll() {
		return this.clienteRepo.findAll().stream()
				.filter(unCli -> unCli.getFechaBaja() == null)
				.collect(Collectors.toList());

	}
	@Override
	public Cliente save(Cliente nuevo) throws RiesgoException {

		if(nuevo.getId() != null) {
			Optional<Cliente> cliente = this.clienteRepo.findById(nuevo.getId());

			if(cliente.isPresent()) 
				return this.clienteRepo.save(nuevo);
			else
				throw new RuntimeException("Cliente no encontrado");
		}
		else if(riesgoService.situacionBCRA(nuevo.getCuit()) > 2) {
			throw new RiesgoException("BCRA");
		}
		else
			return this.clienteRepo.save(nuevo);
	}

	@Override
	public void update(Integer id, Cliente nuevo) throws RuntimeException {
		
		Optional<Cliente> cliente = clienteRepo.findById(id);

		if(cliente.isPresent()) {
			nuevo.setId(id);
			this.clienteRepo.save(nuevo);
//			Cliente clienteDb = cliente.get();
//			clienteDb.setCuit(nuevo.getCuit());
//			clienteDb.setHabilitadoOnline(nuevo.getHabilitadoOnline());
//			clienteDb.setMail(nuevo.getMail());
//			clienteDb.setMaxCuentaCorriente(nuevo.getMaxCuentaCorriente());
//			clienteDb.setObras(nuevo.getObras());
//			clienteDb.setRazonSocial(nuevo.getRazonSocial());
//			clienteDb.setUser(nuevo.getUser());
//			clienteDb.setFechaBaja(nuevo.getFechaBaja());
//			this.clienteRepo.save(clienteDb);
		}
		else 
			throw new RuntimeException("Cliente no encontrado");
	}

	@Override
	public void delete(Integer id) {

		Optional<Cliente> cliente;
		cliente = clienteRepo.findById(id);

		if(cliente.isPresent()) {

			if(!pedidoService.hayPedidos(null,id))
				this.clienteRepo.deleteById(id);
			else {
				//Dar de baja en 30 días
				LocalDate fecha = LocalDate.now();
				fecha = fecha.plusDays(30);
				cliente.get().setFechaBaja(fecha);
				this.clienteRepo.save(cliente.get());
			}
		}
		else
			throw new RuntimeException("Cliente no encontrado");

	}
}
