package ms.usuario.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="USR_TIPO_OBRA", uniqueConstraints = {@UniqueConstraint(columnNames={"descripcion"})})
public class TipoObra {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_tipo_obra")
    private Integer id;
    private String descripcion;
}
