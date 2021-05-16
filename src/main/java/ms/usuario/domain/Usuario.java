package ms.usuario.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="USR_USUARIO", schema = "MS_USR", uniqueConstraints = {@UniqueConstraint(columnNames={"user"})})
public class Usuario {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_usuario")
    private Integer id;
    private String user;
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;
    
}
