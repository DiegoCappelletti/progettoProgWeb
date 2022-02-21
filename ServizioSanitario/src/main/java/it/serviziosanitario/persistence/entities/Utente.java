/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.entities;

/**
 *
 * @author loghi
 */
public class Utente {
    private int utenteId;
    private String email;
    private String hash;
    private String salt;
    private String ruoloUtente;

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRuoloUtente() {
        return ruoloUtente;
    }

    public void setRuoloUtente(String ruoloUtente) {
        this.ruoloUtente = ruoloUtente;
    }

    @Override
    public String toString() {
        return "Utente{" + "utenteId=" + utenteId + ", email=" + email + ", hash=" + hash + ", salt=" + salt + ", ruoloUtente=" + ruoloUtente + '}';
    }
    
    
}
