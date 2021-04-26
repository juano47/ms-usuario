package ms.usuario.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TipoObra {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_tipo_obra")
    private Integer id;
    private String descripcion;
}
