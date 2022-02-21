/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.entities;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author loghi
 */
public class CookieApp {
    private int utenteId;
    private String token;
    private Timestamp dataCreazione;
    private Timestamp dataScadenza;
    private int cookieId;

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Timestamp getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Timestamp dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public int getCookieId() {
        return cookieId;
    }

    public void setCookieId(int cookieId) {
        this.cookieId = cookieId;
    }

    @Override
    public String toString() {
        return "Cookie{" + "utenteId=" + utenteId + ", token=" + token + ", dataCreazione=" + dataCreazione + ", dataScadenza=" + dataScadenza + ", cookieId=" + cookieId + '}';
    }
    
    
}
