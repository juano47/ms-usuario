package ms.usuario.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import ms.usuario.service.RiesgoCrediticioService;

@Service
public class RiesgoServiceImpl implements RiesgoCrediticioService {

	@Override
	public Integer situacionBCRA(String cuit) {
		//Retorna entero aleatorio entre min y max-1
		return ThreadLocalRandom.current().nextInt(0, 5);
	}

}
