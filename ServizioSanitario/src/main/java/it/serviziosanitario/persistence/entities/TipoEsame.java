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
public class TipoEsame {
    private String nome;
    private float costo;
    private int TipiEsameId;

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

    public int getTipiEsameId() {
        return TipiEsameId;
    }

    public void setTipiEsameId(int TipiEsameId) {
        this.TipiEsameId = TipiEsameId;
    }

    @Override
    public String toString() {
        return "TipoEsame{" + "nome=" + nome + ", costo=" + costo + ", TipiEsameId=" + TipiEsameId + '}';
    }
    
}
