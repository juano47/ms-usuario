package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Obra {
    private Integer id;
    private String descripcion;
    private Float latitud;
    private Float longitud;
    private String direccion;
    private Integer superficie;
    private TipoObra tipo;
    private Cliente cliente;
}
