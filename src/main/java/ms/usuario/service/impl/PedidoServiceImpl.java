package ms.usuario.service.impl;


import org.springframework.stereotype.Service;

import ms.usuario.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Override
	public boolean hayPedidos(String cuit, Integer id) {
		return false;
	}

}
