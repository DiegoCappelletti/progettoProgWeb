/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.entities;

import java.sql.Timestamp;

/**
 *
 * @author loghi
 */
public class LinkChangePass {
    private int utenteId;
    private String link;
    private Timestamp dataScadenza;
    
    public int utenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public Timestamp getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Timestamp dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    @Override
    public String toString() {
        return "Cookie{" + "utenteId=" + utenteId + ", link=" + link + ", dataScadenza=" + dataScadenza + '}';
    }
    
    
}
