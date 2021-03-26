package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Empleado {
    private Integer id;
    private String mail;
    private Usuario user;
}
