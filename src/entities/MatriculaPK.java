/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author usuario
 */
@Embeddable
public class MatriculaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ALUMNO_DNI")
    private String alumnoDni;
    @Basic(optional = false)
    @Column(name = "CURSO_ID")
    private int cursoId;

    public MatriculaPK() {
    }

    public MatriculaPK(String alumnoDni, int cursoId) {
        this.alumnoDni = alumnoDni;
        this.cursoId = cursoId;
    }

    public String getAlumnoDni() {
        return alumnoDni;
    }

    public void setAlumnoDni(String alumnoDni) {
        this.alumnoDni = alumnoDni;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        this.cursoId = cursoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alumnoDni != null ? alumnoDni.hashCode() : 0);
        hash += (int) cursoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatriculaPK)) {
            return false;
        }
        MatriculaPK other = (MatriculaPK) object;
        if ((this.alumnoDni == null && other.alumnoDni != null) || (this.alumnoDni != null && !this.alumnoDni.equals(other.alumnoDni))) {
            return false;
        }
        if (this.cursoId != other.cursoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MatriculaPK[ alumnoDni=" + alumnoDni + ", cursoId=" + cursoId + " ]";
    }
    
}
