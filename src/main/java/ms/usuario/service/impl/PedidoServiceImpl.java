package ms.usuario.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ms.usuario.domain.Cliente;
import ms.usuario.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	private RestTemplate restTemplate = new RestTemplate();

	String puerto = "8081";
	String urlServer = "http://localhost";
	String apiPedido = "api/pedido";
	String server = urlServer+":"+puerto+"/"+apiPedido;


	@Override
	public boolean existenPedidosPendientesCliente(List<Integer> idObras) {

		//		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(server)
		//		        // Add query parameter
		//		        .queryParam("firstName", "Mark")
		//		        .queryParam("lastName", "Watney");

		ResponseEntity<Boolean> respuesta = restTemplate.exchange(server+"/obra", HttpMethod.PUT,new HttpEntity<List<Integer>>(idObras) , Boolean.class);
		return respuesta.getBody();
	}

	@Override
	public boolean hayPedidos(String cuit, Integer id) {

		return true;
	}

}
