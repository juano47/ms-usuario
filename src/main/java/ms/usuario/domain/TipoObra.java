package ms.usuario.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"descripcion"})})
public class TipoObra {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_tipo_obra")
    private Integer id;
    private String descripcion;
}
