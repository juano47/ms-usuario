package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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

    @GetMapping(path = "/cuit/{cuit}")
    @ApiOperation(value = "Busca un cliente por cuit")
    public ResponseEntity<Cliente> clientePorCuit(@PathVariable String cuit){

        Optional<Cliente> cliente = clienteService.findByCuit(cuit);
        return ResponseEntity.of(cliente);
    }

    @GetMapping(params = "razonSocial")
    @ApiOperation(value = "Busca un cliente por razón social")
    public ResponseEntity<Cliente> clientePorRazonSocial(@RequestParam Optional<String> razonSocial){

        Optional<Cliente> cliente = clienteService.findByRazonSocial(razonSocial);
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
    public ResponseEntity crear(@RequestBody Cliente cliente){
    	
    	if(cliente.getObras() == null || cliente.getObras().isEmpty()) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body("El cliente debe tener asignado al menos una obra");
    	}else {
    		for (Obra obra : cliente.getObras()) {
				if(obra.getTipo() == null) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    				.body("Las obras asignadas al cliente deben tener setado un tipo de obra");
				}
			}
    	}
    	
    	if(cliente.getUser() == null || cliente.getUser().getUser() == null || 
    			cliente.getUser().getPassword() == null) {
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body("El usuario asignado al cliente debe contener usuario y password");
    	}
    	
        System.out.println(" crear cliente "+cliente);
        cliente = clienteService.save(cliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza un cliente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo,  @PathVariable Integer id){
        
    	
    	Optional<Cliente> clienteDb = clienteService.buscarPorId(id);
        if(clienteDb.isPresent()){
            clienteService.update(clienteDb.get(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Elimina un cliente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Eliminado correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Cliente> borrar(@PathVariable Integer id){

    	Optional<Cliente> clienteDb = clienteService.buscarPorId(id);
        if(clienteDb.isPresent()){
            clienteService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}