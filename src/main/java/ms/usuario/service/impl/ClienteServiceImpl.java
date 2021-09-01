package ms.usuario.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.dao.ObraRepository;
import ms.usuario.dao.TipoUsuarioRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.TipoUsuario;
import ms.usuario.domain.Usuario;
import ms.usuario.exceptions.RiesgoException;
import ms.usuario.service.ClienteService;
import ms.usuario.service.PedidoService;
import ms.usuario.service.RiesgoCrediticioService;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	@Value("${endpoint.gestorImpresion.getPdf}")
	private String GET_PDF_ENDPOINT;

	@Autowired
	ClienteRepository clienteRepo;

	@Autowired
	TipoUsuarioRepository tipoUsuarioRepo;
	
	@Autowired
	ObraRepository obraRepo;

	@Autowired
	RiesgoCrediticioService riesgoService;

	@Autowired
	PedidoService pedidoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Inject
	RestTemplate restTemplate;

	@Override
	public Optional<Cliente> buscarPorId(Integer id) {
		Optional<Cliente> cliente = this.clienteRepo.findById(id);
		if(cliente.isPresent() && cliente.get().getFechaBaja() == null)
			return cliente;

		return Optional.empty();
	}

	@Override
	public Optional<Cliente> findByCuit(Optional<String> cuit) {
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
	public Optional<Cliente> findByIdObra(Optional<Integer> idObra) {

		Optional<Cliente> cliente = this.clienteRepo.findByObrasId(idObra);

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

		Optional<TipoUsuario> tipoUsuario = this.tipoUsuarioRepo.findById(nuevo.getUser().getTipoUsuario().getId());

		if(tipoUsuario.isPresent() && tipoUsuario.get().getTipo().equals("Cliente")){

			if(riesgoService.situacionBCRA(nuevo.getCuit()) > 2) {
				throw new RiesgoException("BCRA");
			}
			else {
				Usuario usuario = nuevo.getUser();
				usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
				return this.clienteRepo.save(nuevo);
			}
		}
		else
			throw new RuntimeException("Tipo de usuario no válido");
	}

	@Override
	public void update(Integer id, Cliente nuevo) {

		Optional<Cliente> cliente = clienteRepo.findById(id);

		if(cliente.isPresent()) {
			if(nuevo.getUser().getId() == null || cliente.get().getUser().getId() != nuevo.getUser().getId())
				throw new RuntimeException("ID Usuario no válido");
			else {
				Optional<TipoUsuario> tipoUsuario = this.tipoUsuarioRepo.findById(nuevo.getUser().getTipoUsuario().getId());

				if(tipoUsuario.isPresent() && tipoUsuario.get().getTipo().equals("Cliente")){
					nuevo.setId(id);
					Usuario usuario = nuevo.getUser();
					usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
					this.clienteRepo.save(nuevo);
				}
				else
					throw new RuntimeException("Tipo de usuario no válido");
			}
		}
		else 
			throw new RuntimeException("Cliente no encontrado");
	}

	@Override
	public void delete(Integer id) {

		Optional<Cliente> cliente;
		cliente = clienteRepo.findById(id);

		if(cliente.isPresent() && cliente.get().getFechaBaja()==null) {
			List<Integer> idObrasCliente = obraRepo.findByClienteId(id)
					.stream().map(m -> m.getId())
					.collect(Collectors.toList());
			
			if(!pedidoService.existenPedidosPendientesCliente(idObrasCliente))
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

	@Override
	public byte[] getReportePdf() throws Exception {
		
		List<Cliente> clientes = clienteRepo.findAll();
		
		byte[] pdf = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<Cliente>> request = new HttpEntity<>(clientes, headers);
		ResponseEntity<byte[]> responses = null;
		try {
			responses = restTemplate.postForEntity(GET_PDF_ENDPOINT, request, byte[].class);
			if (responses != null && responses.getBody() != null) {
				pdf = responses.getBody();
			} else {
				throw new NullPointerException("Error al solicitar el pdf al gestor de plantillas");
			}

		} catch (Exception e) {
			throw new Exception(e);
		}
		return pdf;
	}


}
