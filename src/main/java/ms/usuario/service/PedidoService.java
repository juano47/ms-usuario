package ms.usuario.service;

import java.util.Optional;

import ms.usuario.domain.Cliente;

public interface PedidoService {

	Optional<Cliente> buscarPedidos(String cuit, Integer id);

}
