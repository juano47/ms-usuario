package ms.usuario.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.domain.TipoObra;
import ms.usuario.domain.TipoUsuario;
import ms.usuario.domain.Usuario;

//classes = {MsUsuarioApplicationTests.class},
@SpringBootTest(  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerTest {


	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	String puerto;

	private Cliente unCliente;
	private final String urlServer = "http://localhost";
	private final String apiCliente = "api/cliente";


	@BeforeEach
	void setUp() throws Exception {
		unCliente = new Cliente();
		unCliente.setCuit("20403883935");
		unCliente.setMail("unCli@gmail.com");

		Usuario unUsuario = new Usuario();
		unUsuario.setUser("miUsuario");
		unUsuario.setPassword("MiPassword");
		TipoUsuario unTipoUsuario = new TipoUsuario();
		unTipoUsuario.setTipo("Cliente");
		unUsuario.setTipoUsuario(unTipoUsuario);
		unCliente.setUser(unUsuario);

		Obra obra = new Obra();
		obra.setDescripcion("UnaDescripcion");
		TipoObra tipoObra = new TipoObra();
		tipoObra.setDescripcion("HOGAR");
		obra.setTipo(tipoObra);
		//obra.setCliente(unCliente);
		ArrayList<Obra> obrasArray = new ArrayList<Obra>();
		obrasArray.add(obra);
		List<Obra> obras = obrasArray.stream().collect(Collectors.toList());
		unCliente.setObras(obras);
	}


	@Test
	void testCrearClienteFallaUsuarioSinPassword() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.getUser().setPassword(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}


	@Test
	void testCrearClienteFallaUsuarioSinNombreUsuario() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.getUser().setUser(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}



	@Test
	void testCrearClienteFallaSinUsuario() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.setUser(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}

	@Test
	void testCrearClienteFallaObrasNull() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.setObras(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}


	@Test
	void testCrearClienteFallaObrasIsEmpty() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.getObras().clear();

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}


	@Test
	void testCrearClienteFallaObraTipoIsNull() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.getObras().get(0).setTipo(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}


	@Test
	void testCrearClienteFallaCuitIsNull() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);

		unCliente.setCuit(null);

		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);
		ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente , String.class);		

		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

		System.out.println(respuesta.getBody());
	}



	@Test
	void testCrearClienteConExitoOBCRAexception() {
		String server = urlServer+":"+puerto+"/"+apiCliente;
		System.out.println("SERVER "+server);
		
		HttpEntity<Cliente> requestCliente = new HttpEntity<>(unCliente);

			ResponseEntity<String> respuesta = testRestTemplate.exchange(server, HttpMethod.POST,requestCliente, String.class);
			
			assertTrue(respuesta.getStatusCode().equals(HttpStatus.OK) || respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));

			if(respuesta.getStatusCode().equals(HttpStatus.OK))
				assertTrue(respuesta.getBody().equals("Cliente Creado"));
			else
				assertTrue(respuesta.getBody().equals("BCRA"));
			
			System.out.println("Resultado: "+respuesta.getBody());


	}

	//	@Test
	//	void testClientePorId() {
	//
	//	}
	//
	//	@Test
	//	void testClientePorCuit() {
	//		fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	void testClientePorRazonSocial() {
	//		fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	void testTodos() {
	//		fail("Not yet implemented");
	//	}
	//

	//
	//	@Test
	//	void testActualizar() {
	//		fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	void testBorrar() {
	//		fail("Not yet implemented");
	//	}

}
