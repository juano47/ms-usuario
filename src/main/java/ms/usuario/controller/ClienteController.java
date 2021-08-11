package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cliente")
@Api(value = "ClienteController", description = "Permite gestionar los clientes de la empresa")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un cliente por id")
	public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id){
		Optional<Cliente> cliente = clienteService.buscarPorId(id);
		return ResponseEntity.of(cliente);
	}

	@GetMapping(params = "cuit")
	@ApiOperation(value = "Busca un cliente por cuit")
	public ResponseEntity<Cliente> clientePorCuit(@RequestParam Optional<String> cuit){

		Optional<Cliente> cliente = clienteService.findByCuit(cuit);
		return ResponseEntity.of(cliente);
	}

	@GetMapping(params = "razonSocial")
	@ApiOperation(value = "Busca un cliente por razón social")
	public ResponseEntity<Cliente> clientePorRazonSocial(@RequestParam Optional<String> razonSocial){

		Optional<Cliente> cliente = clienteService.findByRazonSocial(razonSocial);
		return ResponseEntity.of(cliente);
	}

	@GetMapping(params = "idObra")
	@ApiOperation(value = "Busca un cliente por idObra")
	public ResponseEntity<Cliente> clientePorIdObra(@RequestParam Optional<Integer> idObra){

		Optional<Cliente> cliente = clienteService.findByIdObra(idObra);
		return ResponseEntity.of(cliente);
	}

	@GetMapping
	@ApiOperation(value = "Retorna lista de clientes")
	public ResponseEntity<List<Cliente>> todos(){
		List<Cliente> allClientes = clienteService.findAll();
		return ResponseEntity.ok(allClientes);
	}

	@PostMapping
	@ApiOperation(value = "Da de alta un nuevo cliente")
	public ResponseEntity<String> crear(@RequestBody Cliente cliente) {

		if(cliente.getObras() == null || cliente.getObras().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El cliente debe tener asignado al menos una obra");
		}else {
			for (Obra obra : cliente.getObras()) {
				if(obra.getTipoObra() == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Las obras asignadas al cliente deben especificar un tipo de obra");
				}
			}
		}

		if(cliente.getUser() == null || cliente.getUser().getUser() == null || 
				cliente.getUser().getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El usuario asignado al cliente debe contener nombre de usuario y password");
		}
		else if(cliente.getUser().getTipoUsuario() == null || !cliente.getUser().getTipoUsuario().getTipo().equals("Cliente"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de usuario no válido");
		else if(cliente.getCuit() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe ingresar el cuit de cliente");
		}

		try {
			cliente = clienteService.save(cliente);
		}
		catch (DataIntegrityViolationException e1) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMostSpecificCause().toString());
		}
		catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e2.getMessage());
		}
		return ResponseEntity.ok("Cliente Creado");
	}

	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualiza un cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Actualizado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> actualizar(@RequestBody Cliente cliente,  @PathVariable Integer id){


		if(cliente.getUser() == null || cliente.getUser().getUser() == null || 
				cliente.getUser().getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("El usuario asignado al cliente debe contener nombre de usuario y password");
		}
		else if(cliente.getUser().getTipoUsuario() == null || !cliente.getUser().getTipoUsuario().getTipo().equals("Cliente"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Tipo de usuario no válido");
		else if(cliente.getCuit() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe ingresar el cuit de cliente");
		}
		try {
			clienteService.update(id, cliente);
		}
		catch (DataIntegrityViolationException e1) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMostSpecificCause().toString());
		}
		catch(Exception e2) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e2.getMessage());

		} 
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Elimina un cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eliminado correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> borrar(@PathVariable Integer id){

		try {
			clienteService.delete(id);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} 
		return ResponseEntity.ok("Cliente "+id+" borrado con éxito");
	}
}