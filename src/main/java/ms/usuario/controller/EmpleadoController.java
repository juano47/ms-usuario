package ms.usuario.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Empleado;
import ms.usuario.service.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/empleados")
@Api(value = "EmpleadoController", description = "Permite gestionar los empleados de la empresa")
public class EmpleadoController {

	@Autowired
	EmpleadoService empleadoService;

	@HystrixCommand(fallbackMethod = "empleadoVacio")
	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un empleado por id")
	public ResponseEntity<Empleado> empleadoPorId(@PathVariable Integer id){
		Optional<Empleado> empleado = empleadoService.buscarPorId(id);
		return ResponseEntity.of(empleado);
	}

	@HystrixCommand(fallbackMethod = "empleadoVacio")
	@GetMapping(params = "nombre")
	@ApiOperation(value = "Busca un empleado por nombre")
	public ResponseEntity<Empleado> empleadoPorNombre(@RequestParam Optional<String> nombre){

		Optional<Empleado> empleado = empleadoService.findByNombre(nombre);
		return ResponseEntity.of(empleado);
	}
	
	@HystrixCommand(fallbackMethod = "listaVacia")
	@GetMapping
	@ApiOperation(value = "Retorna lista de empleados")
	public ResponseEntity<List<Empleado>> todos(){
		List<Empleado> allEmpleados = empleadoService.findAll();
		return ResponseEntity.ok(allEmpleados);
	}

	@HystrixCommand(fallbackMethod = "errorServidor")
	@PostMapping
	@ApiOperation(value = "Da de alta un nuevo empleado")
	public ResponseEntity<String> crear(@RequestBody Empleado empleado) {

		if(empleado.getMail() == null || empleado.getUser() == null || empleado.getUser().getUser() == null || 
				empleado.getUser().getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El usuario asignado al empleado debe contener mail, nombre de usuario y password");
		}
		else if(empleado.getUser().getTipoUsuario() == null || !empleado.getUser().getTipoUsuario().getTipo().equals("Empleado"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Tipo de usuario no válido");
		else if(empleado.getNombre() == null || empleado.getApellido() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe especificar nombre y apellido");
		}

		try {
			empleado = empleadoService.save(empleado);
		}
		catch (DataIntegrityViolationException e1) {			
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMostSpecificCause().toString());
		}
		 catch (Exception e2) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e2.getMessage());
			}
		return ResponseEntity.ok("Empleado Creado");
	}

	@HystrixCommand(fallbackMethod = "empleadoVacio")
	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualiza un empleado")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> actualizar(@RequestBody Empleado empleado,  @PathVariable Integer id){

		if(empleado.getMail() == null || empleado.getUser() == null || empleado.getUser().getUser() == null || 
				empleado.getUser().getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El usuario asignado al empleado debe contener mail, nombre de usuario y password");
		}
		else if(empleado.getUser().getTipoUsuario() == null || !empleado.getUser().getTipoUsuario().getTipo().equals("Empleado"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Tipo de usuario no válido");
		else if(empleado.getNombre() == null || empleado.getApellido() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe especificar nombre y apellido");
		}

		try {
			empleadoService.update(id, empleado);
		}
		catch (DataIntegrityViolationException e2) {			
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e2.getMostSpecificCause().toString());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} 
		return ResponseEntity.ok(empleado);
	}

	@HystrixCommand(fallbackMethod = "errorServidor")
	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Elimina un empleado")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eliminado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> borrar(@PathVariable Integer id){

		try {
			empleadoService.delete(id);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} 
		return ResponseEntity.ok("Empleado "+id+" borrado con éxito");
	}

	public ResponseEntity<Empleado> empleadoVacio(){
		return ResponseEntity.ok(new Empleado());
	}

	public ResponseEntity<List<Cliente>> listaVacia(){
		return ResponseEntity.ok(new ArrayList<>());
	}

	public ResponseEntity<String> errorServidor(){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Microservicio no disponible- Intentelo más tarde");
	}
}