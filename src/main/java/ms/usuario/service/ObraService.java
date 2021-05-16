package ms.usuario.service;

import java.util.List;
import java.util.Optional;

import ms.usuario.domain.Obra;
import ms.usuario.exceptions.RiesgoException;

public interface ObraService {

	Optional<Obra> findById(Integer id);

	List<Obra> findByClienteOrTipoObra(Integer idCliente, String tipoObra);

	Obra save(Obra nuevo, Integer idCliente) throws RiesgoException;

	void update(Integer id, Obra nuevo);

	void delete(Integer id);

	List<Obra> findAll();

}
