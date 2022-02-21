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
public class Visita {
    private int visitaId;
    private Paziente paziente;
    private Medico medicoDiBase;
    private Medico medicoSpecialista;
    private TipoVisita tipoVisita;
    private Timestamp dataErogazione;
    private Timestamp dataVisita;
    private boolean eseguita;
    private String risultati;

    public int getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(int visitaId) {
        this.visitaId = visitaId;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Medico getMedicoDiBase() {
        return medicoDiBase;
    }

    public void setMedicoDiBase(Medico medicoDiBase) {
        this.medicoDiBase = medicoDiBase;
    }

    public Medico getMedicoSpecialista() {
        return medicoSpecialista;
    }

    public void setMedicoSpecialista(Medico medicoSpecialista) {
        this.medicoSpecialista = medicoSpecialista;
    }

    public TipoVisita getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(TipoVisita tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public Timestamp getDataErogazione() {
        return dataErogazione;
    }

    public void setDataErogazione(Timestamp dataErogazione) {
        this.dataErogazione = dataErogazione;
    }

    public Timestamp getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(Timestamp dataVisita) {
        this.dataVisita = dataVisita;
    }

    public boolean isEseguita() {
        return eseguita;
    }

    public void setEseguita(boolean eseguita) {
        this.eseguita = eseguita;
    }

    public String getRisultati() {
        return risultati;
    }

    public void setRisultati(String risultati) {
        this.risultati = risultati;
    }

    @Override
    public String toString() {
        return "Visita{" + "visita_id=" + visitaId + ", paziente=" + paziente + ", medico_di_base=" + medicoDiBase + ", medico_specialista=" + medicoSpecialista + ", tipovisita=" + tipoVisita + ", data_erogazione=" + dataErogazione + ", data_visita=" + dataVisita + ", eseguita=" + eseguita + ", risultati=" + risultati + '}';
    }

    
    
    
    
}

