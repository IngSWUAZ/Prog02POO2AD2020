package mx.edu.uaz.ingsoftware.poo2.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roberto Solis Robles
 */
@Entity
@Table(name = "persona")
@XmlRootElement
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_persona")
    private String emailPersona;
    @Basic(optional = false)

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre_persona")
    private String nombrePersona;
    @Basic(optional = false)

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apellidos_persona")
    private String apellidosPersona;
    @Basic(optional = false)

    @NotNull
    @Column(name = "sexo_persona")
    @Size(min = 1, max = 1)
    private String sexoPersona;
    @Basic(optional = false)

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "calle_num_persona")
    private String calleNumPersona;


    @Size(max = 50)
    @Column(name = "colonia_persona")
    private String coloniaPersona;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_municipio_persona")
    private long idMunicipioPersona;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_entidad_persona")
    private long idEntidadPersona;


    @Size(max = 5)
    @Column(name = "codpostal_persona")
    private String codpostalPersona;
    @Basic(optional = false)

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "telefono_persona")
    private String telefonoPersona;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nac_persona")
    @Temporal(TemporalType.DATE)
    private Date fechaNacPersona;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_institucion_persona")
    private long idInstitucionPersona;

    @Basic(optional = false)
    @NotNull
    @Size(min=8, max = 10)
    @Column(name = "tipo_persona")
    private String tipoPersona;

    public Persona() {
    }

    public Persona(String emailPersona) {
        this.emailPersona = emailPersona;
    }

    public Persona(String emailPersona, String nombrePersona, String apellidosPersona, String sexoPersona, String calleNumPersona, long idMunicipioPersona, long idEntidadPersona, String telefonoPersona, Date fechaNacPersona, long idInstitucionPersona) {
        this.emailPersona = emailPersona;
        this.nombrePersona = nombrePersona;
        this.apellidosPersona = apellidosPersona;
        this.sexoPersona = sexoPersona;
        this.calleNumPersona = calleNumPersona;
        this.idMunicipioPersona = idMunicipioPersona;
        this.idEntidadPersona = idEntidadPersona;
        this.telefonoPersona = telefonoPersona;
        this.fechaNacPersona = fechaNacPersona;
        this.idInstitucionPersona = idInstitucionPersona;
    }

    public String getEmailPersona() {
        return emailPersona;
    }

    public void setEmailPersona(String emailPersona) {
        this.emailPersona = emailPersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getApellidosPersona() {
        return apellidosPersona;
    }

    public void setApellidosPersona(String apellidosPersona) {
        this.apellidosPersona = apellidosPersona;
    }

    public String getSexoPersona() {
        return sexoPersona;
    }

    public void setSexoPersona(String sexoPersona) {
        this.sexoPersona = sexoPersona;
    }

    public String getCalleNumPersona() {
        return calleNumPersona;
    }

    public void setCalleNumPersona(String calleNumPersona) {
        this.calleNumPersona = calleNumPersona;
    }

    public String getColoniaPersona() {
        return coloniaPersona;
    }

    public void setColoniaPersona(String coloniaPersona) {
        this.coloniaPersona = coloniaPersona;
    }

    public long getIdMunicipioPersona() {
        return idMunicipioPersona;
    }

    public void setIdMunicipioPersona(long idMunicipioPersona) {
        this.idMunicipioPersona = idMunicipioPersona;
    }

    public long getIdEntidadPersona() {
        return idEntidadPersona;
    }

    public void setIdEntidadPersona(long idEntidadPersona) {
        this.idEntidadPersona = idEntidadPersona;
    }

    public String getCodpostalPersona() {
        return codpostalPersona;
    }

    public void setCodpostalPersona(String codpostalPersona) {
        this.codpostalPersona = codpostalPersona;
    }

    public String getTelefonoPersona() {
        return telefonoPersona;
    }

    public void setTelefonoPersona(String telefonoPersona) {
        this.telefonoPersona = telefonoPersona;
    }

    public Date getFechaNacPersona() {
        return fechaNacPersona;
    }

    public void setFechaNacPersona(Date fechaNacPersona) {
        this.fechaNacPersona = fechaNacPersona;
    }

    public long getIdInstitucionPersona() {
        return idInstitucionPersona;
    }

    public void setIdInstitucionPersona(long idInstitucionPersona) {
        this.idInstitucionPersona = idInstitucionPersona;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emailPersona != null ? emailPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.emailPersona == null && other.emailPersona != null) || (this.emailPersona != null && !this.emailPersona.equals(other.emailPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s %s",nombrePersona,apellidosPersona);
    }
    
}
