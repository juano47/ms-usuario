package ms.usuario.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.domain.TipoObra;
import ms.usuario.domain.TipoUsuario;
import ms.usuario.domain.Usuario;
import ms.usuario.exceptions.RiesgoException;
import ms.usuario.service.ClienteService;
import ms.usuario.service.PedidoService;
import ms.usuario.service.RiesgoCrediticioService;

@SpringBootTest
public class ClienteServiceImplTest {

	@Autowired
	ClienteService clienteService;
	
	@MockBean
	PedidoService pedidoServ;
	
	@MockBean
	RiesgoCrediticioService riesgoServ;
	
	@MockBean
	ClienteRepository clienteRepo;
	
	Cliente unCliente;
	
	@BeforeEach
	void setUp() throws Exception {
	unCliente = new Cliente();
	unCliente.setCuit("20403883935");
	unCliente.setMail("unCli@gmail.com");
	
	Usuario unUsuario = new Usuario();
	unUsuario.setUsername("miUsuario");
	unUsuario.setPassword("MiPassword");
	TipoUsuario unTipoUsuario = new TipoUsuario();
	unTipoUsuario.setTipo("Cliente");
	unUsuario.setTipoUsuario(unTipoUsuario);
	unCliente.setUser(unUsuario);
	
	Obra obra = new Obra();
	obra.setDescripcion("UnaDescripcion");
	TipoObra tipoObra = new TipoObra();
	tipoObra.setDescripcion("HOGAR");
	obra.setTipoObra(tipoObra);
	//obra.setCliente(unCliente);
	ArrayList<Obra> obrasArray = new ArrayList<Obra>();
	obrasArray.add(obra);
	List<Obra> obras = obrasArray.stream().collect(Collectors.toList());
	unCliente.setObras(obras);
	}
	
	@Test
	void testCrearClienteConExito() {
		//Situacion crediticia OK
		when(riesgoServ.situacionBCRA(any(String.class))).thenReturn(2);
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		Cliente clienteResultado = null;
		RiesgoException excep = null;
		
		try {
			clienteResultado = clienteService.save(unCliente);
		} catch (RiesgoException e) {
			excep = e;
		}
		assertNull(excep);
		assertSame(clienteResultado,unCliente);
		verify(clienteRepo,times(1)).save(any(Cliente.class));
	}
	
	
	@Test
	void testCrearClienteConRiesgoException() {
		//Situacion crediticia es 3
		when(riesgoServ.situacionBCRA(any(String.class))).thenReturn(3);
		
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		Cliente clienteResultado = null;
		RiesgoException excep = null;
		try {
			clienteResultado = clienteService.save(unCliente);
		} catch (RiesgoException e) {
			excep = e;
		}
		
		assertNull(clienteResultado);
		assertNotNull(excep);
		verify(clienteRepo,never()).save(any(Cliente.class));
	}
	
	@Test
	void testDeleteClienteSinPedidos() {
		
		Optional<Cliente> opt = Optional.of(unCliente);
		//Cliente encontrado
		when(clienteRepo.findById(any(Integer.class))).thenReturn(opt);
		
		//No existen pedidos realizados por el cliente
				when(pedidoServ.hayPedidos(ArgumentMatchers.isNull(),any(Integer.class))).thenReturn(false);
		
		doNothing().when(clienteRepo).deleteById(any(Integer.class));
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		
		RuntimeException excep = null;
		
		try {
			clienteService.delete(1);
		} catch (RuntimeException e) {
			excep = e;
		}
		assertNull(excep);
		assertNull(unCliente.getFechaBaja());
		verify(pedidoServ,times(1)).hayPedidos(ArgumentMatchers.isNull(),any(Integer.class));
		verify(clienteRepo,never()).save(any(Cliente.class));
		verify(clienteRepo,times(1)).deleteById(1);
	}
	
	
	@Test
	void testDeleteClienteConPedidos() {

		Optional<Cliente> opt = Optional.of(unCliente);
		//Cliente encontrado
		when(clienteRepo.findById(any(Integer.class))).thenReturn(opt);
		
		//Existen pedidos realizados por el cliente
		when(pedidoServ.hayPedidos(ArgumentMatchers.isNull(),any(Integer.class))).thenReturn(true);
	
		doNothing().when(clienteRepo).deleteById(any(Integer.class));
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		RuntimeException excep = null;
		
		try {
			clienteService.delete(1);
		} catch (RuntimeException e) {
			excep = e;
		}
		assertNull(excep);
		assertNotNull(unCliente.getFechaBaja());
		assertEquals(LocalDate.now().plusDays(30),unCliente.getFechaBaja());
		verify(clienteRepo,never()).deleteById(any(Integer.class));
		verify(clienteRepo,times(1)).save(unCliente);
	}
	
	@Test
	void testDeleteClienteNoEncontrado() {
		
		//Cliente No encontrado
		when(clienteRepo.findById(any(Integer.class))).thenReturn(Optional.empty());
		
		//Existen pedidos realizados por el cliente
		when(pedidoServ.hayPedidos(ArgumentMatchers.isNull(),any(Integer.class))).thenReturn(true);
				
		doNothing().when(clienteRepo).deleteById(any(Integer.class));
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		RuntimeException excep = null;
		
		try {
			clienteService.delete(1);
		} catch (RuntimeException e) {
			excep = e;
		}
		assertNotNull(excep);
		assertEquals(excep.getMessage(),"Cliente no encontrado");
		assertNull(unCliente.getFechaBaja());
		verify(clienteRepo,never()).deleteById(any(Integer.class));
		verify(pedidoServ,never()).hayPedidos(ArgumentMatchers.isNull(),any(Integer.class));
		verify(clienteRepo,never()).save(any(Cliente.class));
	}
	
	@Test
	void testUpdateClienteNoEncontrado() {
		//Cliente No encontrado
		when(clienteRepo.findById(any(Integer.class))).thenReturn(Optional.empty());
		
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		RuntimeException excep = null;
		try {
			clienteService.update(1,unCliente);
		} catch (RuntimeException e) {
			excep = e;
		}
 
		assertNotNull(excep);
		assertEquals(excep.getMessage(),"Cliente no encontrado");
		verify(clienteRepo,never()).save(any(Cliente.class));
	}
	
	@Test
	void testUpdateClienteExito() {
		
		Cliente clienteDb = new Cliente();
		
		Optional<Cliente> opt = Optional.of(clienteDb);
		//Cliente encontrado
		when(clienteRepo.findById(any(Integer.class))).thenReturn(opt);
		
		//Retornar cliente
		when(clienteRepo.save(any(Cliente.class))).thenReturn(unCliente);
		
		RuntimeException excep = null;
		try {
			clienteService.update(1,unCliente);
		} catch (RuntimeException e) {
			excep = e;
		}
 
		assertNull(excep);
		assertEquals(clienteDb,unCliente);
		verify(clienteRepo,times(1)).save(clienteDb);
	}
}
