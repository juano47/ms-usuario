package ms.usuario.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ObraRepository;
import ms.usuario.domain.Obra;
import ms.usuario.service.ObraService;

@Service
public class ObraServiceImpl implements ObraService{
	
	@Autowired
	ObraRepository obraRepository;

	@Override
	public Optional<Obra> findById(Integer id) {
		return obraRepository.findById(id);
	}

	@Override
	public List<Obra> findByClienteOrTipoObra(Integer idCliente, String tipoObra) {
		if(idCliente != null && tipoObra != null) {
			return obraRepository.findByClienteIdAndTipoObraDescripcion(idCliente, tipoObra);
		}else if(idCliente != null) {
			return obraRepository.findByClienteId(idCliente);
		}else if(tipoObra != null) {
			return obraRepository.findByTipoObraDescripcion(tipoObra);
		}
		return null;
	}

	@Override
	public Obra save(Obra nuevo) {
		return obraRepository.save(nuevo);
	}

	@Override
	public Obra update(Obra obraDb, Obra obraUpdate) {
		
		obraDb.setDescripcion(obraUpdate.getDescripcion());
		obraDb.setLatitud(obraUpdate.getLatitud());
		obraDb.setLongitud(obraUpdate.getLongitud());
		obraDb.setDireccion(obraUpdate.getDireccion());
		obraDb.setSuperficie(obraUpdate.getSuperficie());
		obraDb.setTipoObra(obraUpdate.getTipoObra());
		obraDb.setCliente(obraUpdate.getCliente());
		
		return obraRepository.save(obraDb);
	}

	@Override
	public void delete(Integer id) {
		obraRepository.deleteById(id);
	}
}
