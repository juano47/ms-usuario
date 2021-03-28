package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
import ms.usuario.domain.Obra;
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

    private static final List<Obra> listaObras = new ArrayList<>();
    private static Integer ID_GEN = 1;

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un obra por id")
    public ResponseEntity<Obra> obraPorId(@PathVariable Integer id){

        Optional<Obra> c =  listaObras
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping(params = { "idCliente", "tipoObra" })
    @ApiOperation(value = "Busca obras por Cliente y/o tipo de obra")
    public ResponseEntity<List<Obra>> getObras(@RequestParam(required = false) Integer idCliente,
    										   @RequestParam(required = false) String tipoObra){
    	List<Obra> c;

    	if (idCliente == null && tipoObra == null)
    		c = listaObras;
    	if(idCliente != null && tipoObra != null)
    		c = listaObras
    		.stream()
    		.filter(unaObra -> unaObra.getCliente().getId().equals(idCliente) &&
    				unaObra.getTipo().getDescripcion().equals(tipoObra))
    		.collect(Collectors.toList());
    	else
    		c = listaObras
    		.stream()
    		.filter(unaObra -> unaObra.getCliente().getId().equals(idCliente) ||
    				unaObra.getTipo().getDescripcion().equals(tipoObra))
    		.collect(Collectors.toList());

    	return ResponseEntity.ok(c);
    }

    @PostMapping
    public ResponseEntity<Obra> crear(@RequestBody Obra nuevo){
        System.out.println(" crear obra "+nuevo);
        nuevo.setId(ID_GEN++);
        listaObras.add(nuevo);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Obra> actualizar(@RequestBody Obra nuevo,  @PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaObras.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Obra> borrar(@PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaObras.size())
                .filter(i -> listaObras.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaObras.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
