/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.uaz.ingsoftware.poo2.entidades;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robso
 */
@Entity
@Table(name = "sede")
@XmlRootElement
public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sede")
    private long idSede;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_sede")
    private String nombreSede;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_institucion_sede")
    private long idInstitucionSede;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_director_sede")
    private String emailDirectorSede;
    @Size(max = 200)
    @Column(name = "url_sede")
    private String urlSede;

    public Sede() {
    }

    public Sede(Integer idSede) {
        this.idSede = idSede;
    }

    public Sede(Integer idSede, String nombreSede, long idInstitucionSede, String emailDirectorSede) {
        this.idSede = idSede;
        this.nombreSede = nombreSede;
        this.idInstitucionSede = idInstitucionSede;
        this.emailDirectorSede = emailDirectorSede;
    }

    public long getIdSede() {
        return idSede;
    }

    public void setIdSede(long idSede) {
        this.idSede = idSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public long getIdInstitucionSede() {
        return idInstitucionSede;
    }

    public void setIdInstitucionSede(long idInstitucionSede) {
        this.idInstitucionSede = idInstitucionSede;
    }

    public String getEmailDirectorSede() {
        return emailDirectorSede;
    }

    public void setEmailDirectorSede(String emailDirectorSede) {
        this.emailDirectorSede = emailDirectorSede;
    }

    public String getUrlSede() {
        return urlSede;
    }

    public void setUrlSede(String urlSede) {
        this.urlSede = urlSede;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSede);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sede that = (Sede) o;
        return idSede == that.idSede;
    }

    @Override
    public String toString() {
        return nombreSede;
    }
    
}
