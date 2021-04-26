package ms.usuario.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ms.usuario.domain.Obra;
import ms.usuario.service.ObraService;

@Service
public class ObraServiceImpl implements ObraService{

	@Override
	public Optional<Obra> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Obra> findByClienteOrTipoObra(Integer idCliente, String tipoObra) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Obra save(Obra nuevo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Obra> update(Obra obra, Obra nuevo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

}
