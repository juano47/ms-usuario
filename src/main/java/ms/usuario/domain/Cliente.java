package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Cliente {
    private Integer id;
    private String razonSocial;
    private String cuit;
    private String mail;
    private Double maxCuentaCorriente;
    private Boolean habilitadoOnline;
    private Usuario user;
    private List<Obra> obras;
    private LocalDate fechaBaja;

}