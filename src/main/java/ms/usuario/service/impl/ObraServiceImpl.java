package ms.usuario.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.ClienteRepository;
import ms.usuario.dao.ObraRepository;
import ms.usuario.dao.TipoObraRepository;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.domain.TipoObra;
import ms.usuario.exceptions.RiesgoException;
import ms.usuario.service.ObraService;
import ms.usuario.service.RiesgoCrediticioService;

@Service
public class ObraServiceImpl implements ObraService{

	@Autowired
	ObraRepository obraRepository;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	TipoObraRepository tipoObraRepository;

	@Autowired
	RiesgoCrediticioService riesgoService;

	@Override
	public Optional<Obra> findById(Integer id) {
		return obraRepository.findById(id);
	}

	@Override
	public List<Obra> findByClienteOrTipoObra(Integer idCliente, String tipoObra) {
		if(idCliente != null && tipoObra != null && tipoObra.length()>1) {
			return obraRepository.findByClienteIdAndTipoObraDescripcion(idCliente, tipoObra);
		}else if(idCliente != null) {
			return obraRepository.findByClienteId(idCliente);
		}else if(tipoObra != null) {
			return obraRepository.findByTipoObraDescripcion(tipoObra);
		}
		return null;
	}

	@Override
	public Obra save(Obra nuevo, Integer idCliente) throws RiesgoException {

		Optional<Cliente> cliente = this.clienteRepository.findById(idCliente);

		if(!cliente.isPresent())
			throw new RuntimeException("Cliente no encontrado");
		else {
			Optional<TipoObra> tipoObra = this.tipoObraRepository.findById(nuevo.getTipoObra().getId());

			if(!tipoObra.isPresent() || !tipoObra.get().getId().equals(nuevo.getTipoObra().getId()))
				throw new RuntimeException("Tipo de obra no encontrada");
			else {	
				if(riesgoService.situacionBCRA(cliente.get().getCuit()) > 2) {
					throw new RiesgoException("BCRA");
				}
				else {
					nuevo.setCliente(cliente.get());
					return this.obraRepository.save(nuevo);
				}
			}
		}

	}

	@Override
	public void update(Integer id, Obra nuevo) {

		Optional<Obra> obra = this.obraRepository.findById(id);

		if(obra.isPresent()) {
			nuevo.setId(id);
			nuevo.setCliente(obra.get().getCliente());
			this.obraRepository.save(nuevo);
		}
		else 
			throw new RuntimeException("Obra no encontrada");
	}

	@Override
	public void delete(Integer id) {

		Optional<Obra> obra;
		obra = this.obraRepository.findById(id);

		if(obra.isPresent())
			this.obraRepository.deleteById(id);
		else
			throw new RuntimeException("Obra no encontrada");

	}

	@Override
	public List<Obra> findAll() {
		return this.obraRepository.findAll();
	}
}
