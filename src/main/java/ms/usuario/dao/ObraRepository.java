package ms.usuario.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.usuario.domain.Obra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer>{

	List<Obra> findByClienteIdAndTipoObraDescripcion(Integer idCliente, String tipoObra);

	List<Obra> findByClienteId(Integer idCliente);

	List<Obra> findByTipoObraDescripcion(String tipoObra);

}
