/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.entities;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author loghi
 */
public class Paziente extends Utente{
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private char sesso;  //M or F
    private String indirizzo;
    private Città città;
    private Date dataNascita;
    private String luogoNascita;
    private Medico medico;
    private Provincia provincia;
    private List<Foto> foto;
    private Date dataUltimaVisita;
    private Date dataUltimaRicetta;

    public Date getDataUltimaVisita() {
        return dataUltimaVisita;
    }

    public void setDataUltimaVisita(Date dataUltimaVisita) {
        this.dataUltimaVisita = dataUltimaVisita;
    }

    public Date getDataUltimaRicetta() {
        return dataUltimaRicetta;
    }

    public void setDataUltimaRicetta(Date dataUltimaRicetta) {
        this.dataUltimaRicetta = dataUltimaRicetta;
    }

    
    public Città getCittà() {
        return città;
    }

    public void setCittà(Città città) {
        this.città = città;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Foto> getFoto() {
        return foto;
    }

    public void setFoto(List<Foto> foto) {
        this.foto = foto;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public char getSesso() {
        return sesso;
    }

    public void setSesso(char sesso) {
        this.sesso = sesso;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Città getCittà_id() {
        return città;
    }

    public void setCittà_id(Città città) {
        this.città = città;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "Paziente{" + "nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale + ", sesso=" + sesso + ", indirizzo=" + indirizzo + ", citt\u00e0=" + città + ", dataNascita=" + dataNascita + ", luogoNascita=" + luogoNascita + ", medico=" + medico + ", provincia=" + provincia + ", foto=" + foto + ", dataUltimaVisita=" + dataUltimaVisita + ", dataUltimaRicetta=" + dataUltimaRicetta + '}';
    }
    
    
}
