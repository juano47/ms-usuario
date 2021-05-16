package ms.usuario.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.usuario.dao.EmpleadoRepository;
import ms.usuario.dao.TipoUsuarioRepository;
import ms.usuario.domain.Empleado;
import ms.usuario.domain.TipoUsuario;
import ms.usuario.service.EmpleadoService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService{

	@Autowired
	EmpleadoRepository empleadoRepo;

	@Autowired
	TipoUsuarioRepository tipoUsuarioRepo;


	@Override
	public Optional<Empleado> buscarPorId(Integer id) {
		Optional<Empleado> empleado = this.empleadoRepo.findById(id);
		if(empleado.isPresent())
			return empleado;

		return Optional.empty();
	}

	@Override
	public Optional<Empleado> findByNombre(Optional<String> Nombre) {
		Optional<Empleado> empleado = this.empleadoRepo.findByNombre(Nombre);

		if(empleado.isPresent())
			return empleado;

		return Optional.empty();
	}


	@Override
	public List<Empleado> findAll() {
		return this.empleadoRepo.findAll();

	}
	@Override
	public Empleado save(Empleado nuevo){

		Optional<TipoUsuario> tipoUsuario = this.tipoUsuarioRepo.findById(nuevo.getUser().getTipoUsuario().getId());

		if(tipoUsuario.isPresent() && tipoUsuario.get().getTipo().equals("Empleado")){
			return this.empleadoRepo.save(nuevo);
		}
		else
			throw new RuntimeException("Tipo de usuario no válido");
	}

	@Override
	public void update(Integer id, Empleado nuevo) {

		Optional<Empleado> empleado = empleadoRepo.findById(id);

		if(empleado.isPresent()) {
			if(nuevo.getUser().getId() == null || empleado.get().getUser().getId() != nuevo.getUser().getId())
				throw new RuntimeException("ID Usuario no válido");
			else {
				Optional<TipoUsuario> tipoUsuario = this.tipoUsuarioRepo.findById(nuevo.getUser().getTipoUsuario().getId());

				if(tipoUsuario.isPresent() && tipoUsuario.get().getTipo().equals("Empleado") ){
					nuevo.setId(id);
					this.empleadoRepo.save(nuevo);
				}
				else
					throw new RuntimeException("Tipo de usuario no válido");
			}
		}
		else 
			throw new RuntimeException("Empleado no encontrado");

	}

	@Override
	public void delete(Integer id) {

		Optional<Empleado> empleado;
		empleado = empleadoRepo.findById(id);

		if(empleado.isPresent())
			this.empleadoRepo.deleteById(id);
		else
			throw new RuntimeException("Empleado no encontrado");

	}


}
