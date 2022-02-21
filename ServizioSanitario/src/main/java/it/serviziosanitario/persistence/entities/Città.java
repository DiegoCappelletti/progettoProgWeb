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
public class Città {
    private int cittàId;
    private String nome;
    private Provincia provincia;

    public int getCittàId() {
        return cittàId;
    }

    public void setCittàId(int cittàId) {
        this.cittàId = cittàId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    
}
