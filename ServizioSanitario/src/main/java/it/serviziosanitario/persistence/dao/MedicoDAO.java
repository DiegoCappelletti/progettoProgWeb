/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao;

import it.serviziosanitario.persistence.entities.Esame;
import it.serviziosanitario.persistence.entities.Farmaco;
import it.serviziosanitario.persistence.entities.Medico;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Visita;
import it.serviziosanitario.services.NomiEsamiService.NomeEsame;
import it.serviziosanitario.services.NomiVisiteService.NomeVisita;
import it.serviziosanitario.services.NomiFarmaciService.NomeFarmaco;
import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import java.util.List;


public interface MedicoDAO extends DAO<Medico, Integer>{

    /**
     * Ritorna tutti i pazienti tranne quello con il codice fiscale del medico
     * @param medico_corrente
     * @return
     * @throws DAOException 
     */
    public List<Paziente> getAllPazienti(Medico medico_corrente) throws DAOException;
    
    /**
     * Ritorna tutti i pazienti che hanno come medico di base quello specificato
     * @param medico
     * @return
     * @throws DAOException 
     */
    public List<Paziente> getPazientiByMedic(Medico medico) throws DAOException;

    public Paziente getPazienteByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Eroga una nuova visita (medico di base)
     * @param paziente_id 
     * @param medico_id
     * @param tipo_visita_id
     * @throws DAOException 
     */
    public void InserisciVisita(int paziente_id, int medico_id, int tipo_visita_id) throws DAOException;
    
    public Visita getVisitaByPrimaryKey(int primaryKey) throws DAOException;
    
    /**
     * Esegui una visita (medico specialista)
     * @param medico
     * @param visita
     * @param risultati
     * @throws DAOException 
     */
    void EseguiVisita(Medico medico, Visita visita, String risultati) throws DAOException;
    
    public List<Visita> getAllVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<Esame> getAllEsamiByPaziente(Paziente p) throws DAOException;
    
    public List<Ricetta> getAllRicetteByPaziente(Paziente pazienteR) throws DAOException;
    
    public Esame getEsameByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<Esame> getEEsamiByPaziente(Paziente p) throws DAOException;
    
    public List<Esame> getNEsamiByPaziente(Paziente p) throws DAOException;
    
    public List<Visita> getEVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<Visita> getNEVisiteByPaziente(Paziente p) throws DAOException;
    
    public List<NomeEsame> getAllNomiEsami() throws DAOException;

    public List<NomeVisita> getAllNomiVisite() throws DAOException;
    
    public List<NomeFarmaco> getAllNomiFarmaci() throws DAOException;
    
    public void ErogaRicetta(int paziente_id, int medico_id, int farmaco_id) throws DAOException;
    
    public void InserisciEsame(int paziente_id, int medico_id, int tipo_esame_id) throws DAOException;
    
    /**
     * Controlla che il paziente ha come medico di base quello specificato
     * @param paziente_id
     * @param medico_id
     * @return
     * @throws DAOException 
     */
    public boolean checkPaziente(int paziente_id, int medico_id) throws DAOException;
    
    public Ricetta getRicettaByPrimaryKey(int primaryKey) throws DAOException;
    
    public List<Farmaco> getAllFarmaci() throws DAOException;
    
    public Farmaco getFarmacoByPrimaryKey(int primaryKey) throws DAOException;
    /**
     * Ritorna tutte le visite di un paziente, non ancora eseguite e che si possono fare solo da un medico con 
     * specializzazione spec_id
     * @param p il paziente
     * @param id_specializzazione id della specializzazione
     * @return
     * @throws DAOException 
     */
    public List<Visita> getNEVisiteByPazienteSpecID(Paziente p, int id_specializzazione) throws DAOException;
}
