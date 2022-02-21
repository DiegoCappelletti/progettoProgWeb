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
public class TipoVisita {
    int tipoVisitaId;
    String nome;
    float costo;
    Specializzazione specializzazione;

    public int getTipoVisitaId() {
        return tipoVisitaId;
    }

    public void setTipoVisitaId(int tipoVisitaId) {
        this.tipoVisitaId = tipoVisitaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Specializzazione getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(Specializzazione specializzazione) {
        this.specializzazione = specializzazione;
    }

    @Override
    public String toString() {
        return "TipoVisita{" + "tipo_visita_id=" + tipoVisitaId + ", nome=" + nome + ", costo=" + costo + ", specializzazione=" + specializzazione + '}';
    }
    
    
}
