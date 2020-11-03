package mx.edu.uaz.ingsoftware.poo2.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="institucion")
@XmlRootElement
public class Institucion implements Serializable {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_institucion")
    private long idInstitucion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_institucion")
    private String nombreInstitucion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre_corto_institucion")
    private String nombreCortoInstitucion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "url_institucion")
    private String urlInstitucion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "calle_num_institucion")
    private String calleNumInstitucion;

    @Size(max = 50)
    @Column(name = "colonia_institucion")
    private String coloniaInstitucion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_municipio_institucion")
    private long idMunicipioInstitucion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_entidad_institucion")
    private long idEntidadInstitucion;

    @Size(max = 5)
    @Column(name = "codpostal_institucion")
    private String codpostalInstitucion;

    @Size(max = 10)
    @Column(name = "telefono_institucion")
    private String telefonoInstitucion;

    public Institucion() {
    }

    public Institucion(long idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public Institucion(long idInstitucion, String nombreInstitucion, String nombreCortoInstitucion, String urlInstitucion, String calleNumInstitucion, long idMunicipioInstitucion, long idEntidadInstitucion) {
        this.idInstitucion = idInstitucion;
        this.nombreInstitucion = nombreInstitucion;
        this.nombreCortoInstitucion = nombreCortoInstitucion;
        this.urlInstitucion = urlInstitucion;
        this.calleNumInstitucion = calleNumInstitucion;
        this.idMunicipioInstitucion = idMunicipioInstitucion;
        this.idEntidadInstitucion = idEntidadInstitucion;
    }

    public long getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(long idInstitucion) {
        this.idInstitucion = idInstitucion;
    }


    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        this.nombreInstitucion = nombreInstitucion;
    }


    public String getNombreCortoInstitucion() {
        return nombreCortoInstitucion;
    }

    public void setNombreCortoInstitucion(String nombreCortoInstitucion) {
        this.nombreCortoInstitucion = nombreCortoInstitucion;
    }


    public String getUrlInstitucion() {
        return urlInstitucion;
    }

    public void setUrlInstitucion(String urlInstitucion) {
        this.urlInstitucion = urlInstitucion;
    }


    public String getCalleNumInstitucion() {
        return calleNumInstitucion;
    }

    public void setCalleNumInstitucion(String calleNumInstitucion) {
        this.calleNumInstitucion = calleNumInstitucion;
    }


    public String getColoniaInstitucion() {
        return coloniaInstitucion;
    }

    public void setColoniaInstitucion(String coloniaInstitucion) {
        this.coloniaInstitucion = coloniaInstitucion;
    }


    public long getIdMunicipioInstitucion() {
        return idMunicipioInstitucion;
    }

    public void setIdMunicipioInstitucion(long idMunicipioInstitucion) {
        this.idMunicipioInstitucion = idMunicipioInstitucion;
    }


    public long getIdEntidadInstitucion() {
        return idEntidadInstitucion;
    }

    public void setIdEntidadInstitucion(long idEntidadInstitucion) {
        this.idEntidadInstitucion = idEntidadInstitucion;
    }


    public String getCodpostalInstitucion() {
        return codpostalInstitucion;
    }

    public void setCodpostalInstitucion(String codpostalInstitucion) {
        this.codpostalInstitucion = codpostalInstitucion;
    }


    public String getTelefonoInstitucion() {
        return telefonoInstitucion;
    }

    public void setTelefonoInstitucion(String telefonoInstitucion) {
        this.telefonoInstitucion = telefonoInstitucion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institucion that = (Institucion) o;
        return idInstitucion == that.idInstitucion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstitucion);
    }

    @Override
    public String toString() {
        return nombreInstitucion;
    }

}
