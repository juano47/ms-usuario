package ms.usuario.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ms.usuario.domain.Cliente;
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

    private static final List<Cliente> listaClientes = new ArrayList<>();
    private static Integer ID_GEN = 1;

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Busca un cliente por id")
    public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id){

        Optional<Cliente> c =  listaClientes
                .stream()
                .filter(unCli -> unCli.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping(path = "/cuit/{cuit}")
    @ApiOperation(value = "Busca un cliente por cuit")
    public ResponseEntity<Cliente> clientePorCuit(@PathVariable String cuit){

        Optional<Cliente> c =  listaClientes
                .stream()
                .filter(unCli -> unCli.getCuit().equals(cuit))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping(params = "razonSocial")
    @ApiOperation(value = "Busca un cliente por razón social")
    public ResponseEntity<Cliente> clientePorRazonSocial(@RequestParam Optional<String> razonSocial){

        Optional<Cliente> c =  listaClientes
                .stream()
                .filter(unCli -> unCli.getRazonSocial().equals(razonSocial.get()))
                .findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> todos(){
        return ResponseEntity.ok(listaClientes);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevo){
        System.out.println(" crear cliente "+nuevo);
        nuevo.setId(ID_GEN++);
        listaClientes.add(nuevo);
        return ResponseEntity.ok(nuevo);
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
        OptionalInt indexOpt =   IntStream.range(0, listaClientes.size())
                .filter(i -> listaClientes.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaClientes.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Cliente> borrar(@PathVariable Integer id){
        OptionalInt indexOpt =   IntStream.range(0, listaClientes.size())
                .filter(i -> listaClientes.get(i).getId().equals(id))
                .findFirst();

        if(indexOpt.isPresent()){
            listaClientes.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}