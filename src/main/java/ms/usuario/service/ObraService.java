package ms.usuario.service;

import java.util.List;
import java.util.Optional;

import ms.usuario.domain.Obra;

public interface ObraService {

	Optional<Obra> findById(Integer id);

	List<Obra> findByClienteOrTipoObra(Integer idCliente, String tipoObra);

	Obra save(Obra nuevo);

	Optional<Obra> update(Obra obra, Obra nuevo);

	void delete(Integer id);

}
