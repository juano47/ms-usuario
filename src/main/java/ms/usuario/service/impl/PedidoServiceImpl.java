package ms.usuario.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${endpoint.msPedido}")
	private String PEDIDO_ENDPOINT;


	@Override
	public boolean existenPedidosPendientesCliente(List<Integer> idObras) {

		String url = PEDIDO_ENDPOINT+"/obra"; 
		
		try {
		ResponseEntity<Boolean> respuesta = restTemplate.exchange(url, HttpMethod.PUT,new HttpEntity<List<Integer>>(idObras) , Boolean.class);
		return respuesta.getBody();
		}catch(Exception e1) {
			throw new RuntimeException("MS-Pedidos no responde. El cliente no pudo ser eliminado. Vuelva a intentarlo");
		}
		
	}

	@Override
	public boolean hayPedidos(String cuit, Integer id) {

		return true;
	}

	//		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(server)
	//		        // Add query parameter
	//		        .queryParam("firstName", "Mark")
	//		        .queryParam("lastName", "Watney");
	
}
