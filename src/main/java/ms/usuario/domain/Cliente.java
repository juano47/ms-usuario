package ms.usuario.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@Table(name="USR_CLIENTE", schema = "MS_USR", uniqueConstraints = {@UniqueConstraint(columnNames={"mail"})})
public class Cliente {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Integer id;
	private String razonSocial;
	private String cuit;
	private String mail;
	private Double maxCuentaCorriente;
	private Boolean habilitadoOnline;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_usuario")
	private Usuario user;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
	@JsonManagedReference
	private List<Obra> obras;
	private LocalDate fechaBaja;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuit == null) ? 0 : cuit.hashCode());
		result = prime * result + ((fechaBaja == null) ? 0 : fechaBaja.hashCode());
		result = prime * result + ((habilitadoOnline == null) ? 0 : habilitadoOnline.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((maxCuentaCorriente == null) ? 0 : maxCuentaCorriente.hashCode());
		result = prime * result + ((obras == null) ? 0 : obras.hashCode());
		result = prime * result + ((razonSocial == null) ? 0 : razonSocial.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (cuit == null) {
			if (other.cuit != null)
				return false;
		} else if (!cuit.equals(other.cuit))
			return false;
		if (fechaBaja == null) {
			if (other.fechaBaja != null)
				return false;
		} else if (!fechaBaja.equals(other.fechaBaja))
			return false;
		if (habilitadoOnline == null) {
			if (other.habilitadoOnline != null)
				return false;
		} else if (!habilitadoOnline.equals(other.habilitadoOnline))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (maxCuentaCorriente == null) {
			if (other.maxCuentaCorriente != null)
				return false;
		} else if (!maxCuentaCorriente.equals(other.maxCuentaCorriente))
			return false;
		if (obras == null) {
			if (other.obras != null)
				return false;
		} else if (!obras.equals(other.obras))
			return false;
		if (razonSocial == null) {
			if (other.razonSocial != null)
				return false;
		} else if (!razonSocial.equals(other.razonSocial))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}

