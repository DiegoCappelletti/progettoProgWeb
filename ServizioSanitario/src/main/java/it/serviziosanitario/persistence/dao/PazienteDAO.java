/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao;

import it.serviziosanitario.persistence.entities.Esame;
import it.serviziosanitario.persistence.entities.Medico;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Visita;
import it.serviziosanitario.services.CodiciFiscaliService.CodiceFiscale;
import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import java.util.List;

/**
 *
 * @author loghi
 */
public interface PazienteDAO extends DAO<Paziente, Integer>{
    /**
     * Dato il medico, ritorna la lista completa dei suoi pazienti
     * @param id_medico
     * @return 
     */
    public List<Paziente> getPazientiByMedic(Medico medico) throws DAOException;
    
    /**
     * Ritorna la lista dei pazienti compresi in un certo range di eta
     * L'id del paziente è utente_id (campo della superclasse)
     * @param min_age   l'eta minima
     * @param max_age   l'eta massima
     * @return
     * @throws DAOException 
     */
    
    
    public List<Medico> getByProvincia(Paziente paziente_corrente) throws DAOException;
    
    public Visita getVisitaByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<Visita> getAllVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<Visita> getEVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<Visita> getNEVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<Esame> getAllEsamiByPaziente(Paziente p) throws DAOException;
    
    public Esame getEsameByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<CodiceFiscale> getAllCodiciFiscali() throws DAOException;
    
    public Ricetta getRicettaByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<Ricetta> getAllRicetteByPaziente(Paziente pazienteR) throws DAOException;
    
    public void insertFoto(Paziente paziente, String path) throws DAOException;
    
    public void deleteFoto(int primaryKey) throws DAOException;
    
    public boolean checkRicetta(int utenteId, int ricettaId) throws DAOException;
    
    public void cambiaMedico (int pazienteId, int medicoId) throws DAOException;
}
