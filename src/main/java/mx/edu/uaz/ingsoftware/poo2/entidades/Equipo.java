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
@Table(name = "equipo")
@XmlRootElement
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_equipo")
    private long  idEquipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nombre_equipo")
    private String nombreEquipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_coach")
    private String emailCoach;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_concursante1")
    private String emailConcursante1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_concursante2")
    private String emailConcursante2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email_concursante3")
    private String emailConcursante3;
    @Size(max = 40)
    @Column(name = "email_concursante_reserva")
    private String emailConcursanteReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_institucion_equipo")
    private long idInstitucionEquipo;

    public Equipo() {
    }

    public Equipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Equipo(long idEquipo, String nombreEquipo, String emailCoach, String emailConcursante1, String emailConcursante2, String emailConcursante3, long idInstitucionEquipo) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.emailCoach = emailCoach;
        this.emailConcursante1 = emailConcursante1;
        this.emailConcursante2 = emailConcursante2;
        this.emailConcursante3 = emailConcursante3;
        this.idInstitucionEquipo = idInstitucionEquipo;
    }

    public long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getEmailCoach() {
        return emailCoach;
    }

    public void setEmailCoach(String emailCoach) {
        this.emailCoach = emailCoach;
    }

    public String getEmailConcursante1() {
        return emailConcursante1;
    }

    public void setEmailConcursante1(String emailConcursante1) {
        this.emailConcursante1 = emailConcursante1;
    }

    public String getEmailConcursante2() {
        return emailConcursante2;
    }

    public void setEmailConcursante2(String emailConcursante2) {
        this.emailConcursante2 = emailConcursante2;
    }

    public String getEmailConcursante3() {
        return emailConcursante3;
    }

    public void setEmailConcursante3(String emailConcursante3) {
        this.emailConcursante3 = emailConcursante3;
    }

    public String getEmailConcursanteReserva() {
        return emailConcursanteReserva;
    }

    public void setEmailConcursanteReserva(String emailConcursanteReserva) {
        this.emailConcursanteReserva = emailConcursanteReserva;
    }

    public long getIdInstitucionEquipo() {
        return idInstitucionEquipo;
    }

    public void setIdInstitucionEquipo(long idInstitucionEquipo) {
        this.idInstitucionEquipo = idInstitucionEquipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo that = (Equipo) o;
        return idEquipo == that.idEquipo;
    }

    @Override
    public String toString() {
        return nombreEquipo;
    }
    
}
