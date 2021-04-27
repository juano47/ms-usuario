package ms.usuario.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Obra {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_obra")
    private Integer id;
    private String descripcion;
    private Float latitud;
    private Float longitud;
    private String direccion;
    private Integer superficie;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_tipo_obra")
    private TipoObra tipoObra;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
