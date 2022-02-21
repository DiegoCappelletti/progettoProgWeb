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
public class Ricetta {
    private int ricettaId;
    private Paziente paziente;
    private Medico medico;
    private Farmaco farmaco;
    private Timestamp dataErogazione;

    public int getRicettaId() {
        return ricettaId;
    }

    public void setRicettaId(int ricettaId) {
        this.ricettaId = ricettaId;
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

    
    public Farmaco getFarmaco() {
        return farmaco;
    }

    public void setFarmaco(Farmaco farmaco) {
        this.farmaco = farmaco;
    }

    

    public Timestamp getDataErogazione() {
        return dataErogazione;
    }

    public void setDataErogazione(Timestamp dataErogazione) {
        this.dataErogazione = dataErogazione;
    }

    @Override
    public String toString() {
        return "Ricetta{" + "ricettaId=" + ricettaId + ", paziente=" + paziente + ", medico=" + medico + ", farmaco=" + farmaco + ", dataErogazione=" + dataErogazione + '}';
    }

    

   
    
}
