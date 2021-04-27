package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
import ms.usuario.service.ObraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @PostMapping
    @ApiOperation(value = "Da de alta una nueva obra")
    public ResponseEntity<Obra> crear(@RequestBody Obra nuevo){
       Obra obra = obraService.save(nuevo);
       return ResponseEntity.ok(obra);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Actualiza una obra")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Actualizada correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Obra> actualizar(@RequestBody Obra nuevo,  @PathVariable Integer id){
       
    	Optional<Obra> obraDbOpt = obraService.findById(id);

        if(obraDbOpt.isPresent()){
        	Obra obraDb = obraService.update(obraDbOpt.get(), nuevo);
            return ResponseEntity.ok(obraDb);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Elimina una obra")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Eliminada correctamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Prohibido"),
            @ApiResponse(code = 404, message = "El ID no existe")
    })
    public ResponseEntity<Obra> borrar(@PathVariable Integer id){

    	Optional<Obra> obraDb = obraService.findById(id);

        if(obraDb.isPresent()){
            obraService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
