/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juanjo
 */
@Entity
@Table(name = "MATRICULA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m"),
    @NamedQuery(name = "Matricula.findByAlumnoDni", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.alumnoDni = :alumnoDni"),
    @NamedQuery(name = "Matricula.findByCursoId", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.cursoId = :cursoId"),
    @NamedQuery(name = "Matricula.findByTipoMatricula", query = "SELECT m FROM Matricula m WHERE m.tipoMatricula = :tipoMatricula"),
    @NamedQuery(name = "Matricula.findByPK", query = "SELECT m FROM Matricula m WHERE m.matriculaPK.cursoId = :cursoId AND m.matriculaPK.alumnoDni = :alumnoDni"),
    @NamedQuery(name = "Matricula.findByFecha", query = "SELECT m FROM Matricula m WHERE m.fecha = :fecha"),
    @NamedQuery(name = "Matricula.findByDocumentacion", query = "SELECT m FROM Matricula m WHERE m.documentacion = :documentacion"),
    @NamedQuery(name = "Matricula.findByCertificado", query = "SELECT m FROM Matricula m WHERE m.certificado = :certificado"),
    @NamedQuery(name = "Matricula.findByImporteAbonado", query = "SELECT m FROM Matricula m WHERE m.importeAbonado = :importeAbonado")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatriculaPK matriculaPK;
    @Basic(optional = false)
    @Column(name = "TIPO_MATRICULA")
    private String tipoMatricula;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "DOCUMENTACION")
    private Boolean documentacion;
    @Basic(optional = false)
    @Column(name = "CERTIFICADO")
    private Boolean certificado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "IMPORTE_ABONADO")
    private BigDecimal importeAbonado;
    @JoinColumn(name = "ALUMNO_DNI", referencedColumnName = "DNI", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Alumno alumno;
    @JoinColumn(name = "CURSO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Curso curso;

    public Matricula() {
    }

    public Matricula(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public Matricula(MatriculaPK matriculaPK, String tipoMatricula, Date fecha, Boolean documentacion, Boolean certificado, BigDecimal importeAbonado) {
        this.matriculaPK = matriculaPK;
        this.tipoMatricula = tipoMatricula;
        this.fecha = fecha;
        this.documentacion = documentacion;
        this.certificado = certificado;
        this.importeAbonado = importeAbonado;
    }

    public Matricula(String alumnoDni, int cursoId) {
        this.matriculaPK = new MatriculaPK(alumnoDni, cursoId);
    }

    public MatriculaPK getMatriculaPK() {
        return matriculaPK;
    }

    public void setMatriculaPK(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public String getTipoMatricula() {
        return tipoMatricula;
    }

    public void setTipoMatricula(String tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(Boolean documentacion) {
        this.documentacion = documentacion;
    }

    public Boolean getCertificado() {
        return certificado;
    }

    public void setCertificado(Boolean certificado) {
        this.certificado = certificado;
    }

    public BigDecimal getImporteAbonado() {
        return importeAbonado;
    }

    public void setImporteAbonado(BigDecimal importeAbonado) {
        this.importeAbonado = importeAbonado;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaPK != null ? matriculaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.matriculaPK == null && other.matriculaPK != null) || (this.matriculaPK != null && !this.matriculaPK.equals(other.matriculaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Matricula[ matriculaPK=" + matriculaPK + " ]";
    }
    
}
