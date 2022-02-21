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
import it.serviziosanitario.persistence.entities.Ssp;
import it.serviziosanitario.persistence.entities.TipoEsame;
import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import java.util.List;

/**
 *
 * @author loghi
 */
public interface SspDAO extends DAO <Ssp, Integer>{
    public Paziente getPazienteByPrimaryKey(Integer primaryKey) throws DAOException;
    
    public List<Paziente> getPazientiByAgeRange(int min_age, int max_age) throws Exception;
    
    public List<Esame> getAllEsamiByPaziente(Paziente p) throws DAOException;
    
    void InserisciEsame(Paziente paziente, Medico medico, TipoEsame tipo) throws DAOException;
     
    void EseguiEsame(Ssp ssp, Esame esame, String risultati) throws DAOException;
     
    public Esame getEsameByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<Esame> getEEsamiByPaziente(Paziente p) throws DAOException;
    
    public List<Esame> getNEsamiByPaziente(Paziente p) throws DAOException;
    
    public List<Paziente> getAllPazienti() throws DAOException;
    
    public List<Ricetta> getAllRicetteByIdProvincia(int provinciaId) throws DAOException;
}
