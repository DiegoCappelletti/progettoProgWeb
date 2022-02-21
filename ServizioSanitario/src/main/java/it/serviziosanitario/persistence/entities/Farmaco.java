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
public class Farmaco {
    private int farmacoId;
    private String nome;
    private float costo;

    public int getFarmacoId() {
        return farmacoId;
    }

    public void setFarmacoId(int farmacoId) {
        this.farmacoId = farmacoId;
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
    
}
