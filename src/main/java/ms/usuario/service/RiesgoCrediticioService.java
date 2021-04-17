package ms.usuario.service;

import ms.usuario.exceptions.RiesgoException;

public interface RiesgoCrediticioService {

	Integer situacionBCRA(String cuit);
	
}
