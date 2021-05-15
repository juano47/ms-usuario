package ms.usuario.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="USR_EMPLEADO", schema = "MS_USR", uniqueConstraints = {@UniqueConstraint(columnNames={"mail"})})
public class Empleado {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_empleado")
    private Integer id;
    private Long dni;
    private String nombre;
    private String apellido;
    private String mail;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_usuario")
    private Usuario user;
}
