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
public class Esame {
    private int esameId;
    private Paziente paziente;
    private Medico medico;
    private Ssp ssp;
    private TipoEsame tipoEsame;
    private Timestamp dataErogazione;
    private Timestamp dataEsame;
    private boolean eseguito;
    private String risultati;

    public int getEsameId() {
        return esameId;
    }

    public void setEsameId(int EsameId) {
        this.esameId = EsameId;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Ssp getSsp() {
        return ssp;
    }

    public void setSsp(Ssp ssp) {
        this.ssp = ssp;
    }

    public TipoEsame getTipoEsame() {
        return tipoEsame;
    }

    public void setTipoEsame(TipoEsame tipoEsame) {
        this.tipoEsame = tipoEsame;
    }

    
    public Timestamp getDataErogazione() {
        return dataErogazione;
    }

    public void setDataErogazione(Timestamp dataErogazione) {
        this.dataErogazione = dataErogazione;
    }

    public Timestamp getDataEsame() {
        return dataEsame;
    }

    public void setDataEsame(Timestamp dataEsame) {
        this.dataEsame = dataEsame;
    }

    public boolean isEseguito() {
        return eseguito;
    }

    public void setEseguito(boolean eseguito) {
        this.eseguito = eseguito;
    }

    public String getRisultati() {
        return risultati;
    }

    public void setRisultati(String risultati) {
        this.risultati = risultati;
    }

    @Override
    public String toString() {
        return "Esame{" + "esameId=" + esameId + ", paziente=" + paziente + ", medico=" + medico + ", ssp=" + ssp + ", tipoEsame=" + tipoEsame + ", dataErogazione=" + dataErogazione + ", dataEsame=" + dataEsame + ", eseguito=" + eseguito + ", risultati=" + risultati + '}';
    }
    
}
