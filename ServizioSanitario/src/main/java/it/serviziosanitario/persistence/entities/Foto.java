/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.entities;

import java.sql.Date;

/**
 *
 * @author loghi
 */
public class Foto {
    private int fotoId;
    private int pazienteId;
    private String path;
    private Date data;

    public int getFotoId() {
        return fotoId;
    }

    public void setFotoId(int fotoId) {
        this.fotoId = fotoId;
    }

    public int getPazienteId() {
        return pazienteId;
    }

    public void setPazienteId(int pazienteId) {
        this.pazienteId = pazienteId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Foto{" + "fotoId=" + fotoId + ", pazienteId=" + pazienteId + ", path=" + path + ", data=" + data + '}';
    }
    
    
}
