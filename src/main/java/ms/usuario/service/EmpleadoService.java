package ms.usuario.service;

import java.util.List;
import java.util.Optional;

import ms.usuario.domain.Empleado;

public interface EmpleadoService {

	Optional<Empleado> buscarPorId(Integer id);

	Optional<Empleado> findByNombre(Optional<String> Nombre);

	List<Empleado> findAll();

	Empleado save(Empleado cliente);

	void update(Integer id, Empleado nuevo);

	void delete(Integer id);

}
