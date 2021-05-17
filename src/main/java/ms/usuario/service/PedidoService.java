package ms.usuario.service;

import java.util.List;

public interface PedidoService {

	boolean existenPedidosPendientesCliente(List<Integer> idObras);

	boolean hayPedidos(String cuit, Integer id);

}
