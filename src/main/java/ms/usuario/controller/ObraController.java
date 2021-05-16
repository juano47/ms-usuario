package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Obra;
import ms.usuario.service.ObraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/obra")
@Api(value = "ObraController", description = "Permite gestionar los obras de la empresa")
public class ObraController {

	@Autowired
	ObraService obraService;

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Busca un obra por id")
	public ResponseEntity<Obra> obraPorId(@PathVariable Integer id){
		Optional<Obra> obra = obraService.findById(id);
		return ResponseEntity.of(obra);
	}

	@GetMapping(params = { "idCliente", "tipoObra" })
	@ApiOperation(value = "Busca obras por cliente y/o tipo de obra")
	public ResponseEntity<List<Obra>> getObras(@RequestParam(required = false) Integer idCliente,
			@RequestParam(required = false) String tipoObra){
		List<Obra> obras = obraService.findByClienteOrTipoObra(idCliente, tipoObra);
		return ResponseEntity.ok(obras);
	}

	@GetMapping
	@ApiOperation(value = "Retorna lista de obras")
	public ResponseEntity<List<Obra>> todas(){
		List<Obra> allObras = obraService.findAll();
		return ResponseEntity.ok(allObras);
	}

	@PostMapping(path = "/{idCliente}")
	@ApiOperation(value = "Da de alta una nueva obra")
	public ResponseEntity<?> crear(@RequestBody Obra nuevo, @PathVariable Integer idCliente){

		if(idCliente == null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe especificar id del cliente");
		}
		else
			if(nuevo.getTipoObra() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("La obra debe especificar un tipo de obra");
			}
		try {
			obraService.save(nuevo, idCliente);
		}
		catch (DataIntegrityViolationException e1) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e1.getMostSpecificCause().toString());
		}catch (Exception e2) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e2.getMessage());
		}

		return ResponseEntity.ok("Obra creada");

	}

	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Actualiza una obra")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Actualizada correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> actualizar(@RequestBody Obra nuevo,  @PathVariable Integer id){

		if(nuevo.getTipoObra() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("La obra debe especificar un tipo de obra");
		}
		try {
			obraService.update(id, nuevo);
		}
		catch (DataIntegrityViolationException e2) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e2.getMostSpecificCause().toString());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} 
		return ResponseEntity.ok(nuevo);
	}

	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Elimina una obra")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eliminada correctamente"),
			@ApiResponse(code = 401, message = "No autorizado"),
			@ApiResponse(code = 403, message = "Prohibido"),
			@ApiResponse(code = 404, message = "El ID no existe")
	})
	public ResponseEntity<?> borrar(@PathVariable Integer id){

		try {
			obraService.delete(id);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} 
		return ResponseEntity.ok("Obra "+id+" borrada con éxito");
	}
}
