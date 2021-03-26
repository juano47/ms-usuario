package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private Integer id;
    private String user;
    private String password;
    private TipoUsuario tipoUsuario;
}
