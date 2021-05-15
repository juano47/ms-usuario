package ms.usuario.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.usuario.domain.TipoObra;

@Repository
public interface TipoObraRepository extends JpaRepository<TipoObra, Integer> {

	Optional<TipoObra> findById(Integer id);

}