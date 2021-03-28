package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Empleado {
    private Integer id;
    private Long dni;
    private String nombre;
    private String apellido;
    private String mail;
    private Usuario user;
}
