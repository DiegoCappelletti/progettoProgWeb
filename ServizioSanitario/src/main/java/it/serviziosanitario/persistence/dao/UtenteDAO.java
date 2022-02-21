/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao;

import it.serviziosanitario.persistence.entities.CookieApp;
import it.serviziosanitario.persistence.entities.LinkChangePass;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.DAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;

/**
 *
 * @author loghi
 */
public interface UtenteDAO extends DAO<Utente, Integer>{
    /**
     * Ritorna un utene data la sua email
     * @param email
     * @return 
     */
    public Utente getByEmail (String email) throws DAOException;
    
    public void updateUtente (Utente utente) throws DAOException;
    
    public void creaCookie(Utente utente, int seconds, String token) throws DAOException;
    
    public CookieApp checkCookie(String token) throws DAOException;
    
    public void deleteCookie(int utenteId) throws DAOException;
    
    public void creaLink(Utente utente, String link) throws DAOException;
    
    public LinkChangePass checkLink(String link) throws DAOException;
    
    public void deleteLink(int utenteId) throws DAOException;
}
