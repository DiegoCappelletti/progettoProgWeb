/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao.jdbc;


import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.CookieApp;
import it.serviziosanitario.persistence.entities.LinkChangePass;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author loghi
 */
public class JDBCUtenteDAO extends JDBCDAO<Utente, Integer> implements UtenteDAO{
    
    public JDBCUtenteDAO(Connection con) {
        super(con);
    }
    
    @Override
    public Long getCount() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente getByPrimaryKey(Integer primaryKey) throws DAOException {
        if(primaryKey==null){
            throw new DAOException("primaryKey is null");
        }
        Utente utente = new Utente();
        String query = "SELECT * FROM utenti WHERE utente_id=?";
        
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("ERRORE: Constrant 'utente_id unico' violato");
                    }
                    
                    utente.setUtenteId(rs.getInt("utente_id"));
                    utente.setEmail(rs.getString("email"));
                    utente.setHash(rs.getString("hash"));
                    utente.setSalt(rs.getString("salt"));
                    utente.setRuoloUtente(rs.getString("ruolo_utente"));

                }
                //se non è stato trovato nessun utente con quella primary key
                if(count == 0){
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return utente;
    }

    @Override
    public List<Utente> getAll() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Utente getByEmail(String email) throws DAOException{
        if (email.isEmpty()) {
            throw new DAOException("Email mancante");
        }
        Utente utente = new Utente();
        email = email.substring(0).toLowerCase();
        String query = "SELECT * FROM utenti WHERE email=?";
        
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setString(1, email);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("ERRORE: Constrant 'email unica' violato");
                    }
                    
                    
                    
                    utente.setUtenteId(rs.getInt("utente_id"));
                    utente.setEmail(rs.getString("email"));
                    utente.setHash(rs.getString("hash"));
                    utente.setSalt(rs.getString("salt"));
                    utente.setRuoloUtente(rs.getString("ruolo_utente"));

                    
                }
                if(count==0){
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return utente;
        
    }

    @Override
    public void updateUtente(Utente utente) throws DAOException {
        if (utente==null){
            throw new DAOException("L'utente da aggiornare è null");
        }
        try (PreparedStatement ps = CON.prepareStatement("UPDATE utenti SET email = ?, hash = ?, salt = ? WHERE utente_id = ?")) {
            
            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getHash());
            ps.setString(3, utente.getSalt());
            ps.setInt(4, utente.getUtenteId());
            
            int count = ps.executeUpdate();
            if (count >1) {
                throw new DAOException("Update effettuato su piu utenti: " + count); //dovrebbe essere impossibile
            }
            if (count ==0){
                throw new DAOException("Nessun update effettuato! (Probabilmente non esiste un utente con quel id) "); //errore id utente
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile aggiornare l'utente con id="+utente.getUtenteId(), ex);
        }
    }

    @Override
    public void creaCookie(Utente utente, int seconds, String token) throws DAOException {
        String query = "INSERT INTO COOKIE (utente_id, token, data_creazione, data_scadenza)\n" +
                        "VALUES (?, ?, ?, ?)";
        
        
        Timestamp data_creazione = new Timestamp(System.currentTimeMillis());
        //Timestamp data_scadenza = new Timestamp(System.currentTimeMillis() + seconds * 1000);
        
        
        Timestamp data_scadenza = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(data_scadenza);
        cal.add(Calendar.SECOND, seconds-3600); //per qualche motivo i cookie hanno un ora in meno
        data_scadenza.setTime(cal.getTime().getTime()); // or
        data_scadenza = new Timestamp(cal.getTime().getTime());
        
        
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            
            ps.setInt(1, utente.getUtenteId());
            ps.setString(2, token);
            ps.setTimestamp(3, data_creazione);
            ps.setTimestamp(4, data_scadenza);
           
            
            int count = ps.executeUpdate();
            
            if (count == 0) {
                throw new DAOException("Cookie non creato");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile creare il cookie");
        }
    }

    @Override
    public CookieApp checkCookie(String token) throws DAOException {
        String query = "SELECT utente_id, token, data_creazione, data_scadenza, cookie_id\n" +
                        "FROM COOKIE \n" +
                        "WHERE token = ?";
        CookieApp daRitornare = null;
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setString(1, token);
            try (ResultSet rs = stm.executeQuery()) {
                
                while (rs.next()) {
                    
                    daRitornare = new CookieApp();
                    daRitornare.setCookieId(rs.getInt("cookie_id"));
                    daRitornare.setUtenteId(rs.getInt("utente_id"));
                    daRitornare.setToken(rs.getString("token"));
                    daRitornare.setDataCreazione(rs.getTimestamp("data_creazione"));
                    daRitornare.setDataScadenza(rs.getTimestamp("data_scadenza"));
                    
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        query = "DELETE \n" +
                "FROM COOKIE \n" +
                "WHERE current_timestamp > data_scadenza";
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            
            stm.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Impossibile eliminare il cookie", ex);
        }
        
        return daRitornare;
        
    }

    @Override
    public void deleteCookie(int utenteId) throws DAOException {
        String query = "DELETE \n" +
                "FROM COOKIE \n" +
                "WHERE utente_id = ?";
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, utenteId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Impossibile eliminare il cookie", ex);
        }
    }
    
    @Override
    public void creaLink(Utente utente, String link) throws DAOException {
        
        String query = "INSERT INTO LINK (utente_id, link, scadenza)\n" +
                        "VALUES (?, ?, ?)";
        
        int day = 24; //ore presenti in un giorno.
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.HOUR, day);
        
        Timestamp data_scadenza = new Timestamp(cal.getTime().getTime());
        
        
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            
            ps.setInt(1, utente.getUtenteId());
            ps.setString(2, link);
            ps.setTimestamp(3, data_scadenza);
           
            
            int count = ps.executeUpdate();
            
            if (count == 0) {
                throw new DAOException("Link non creato");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile creare il link");
        }
    }

    @Override
    public LinkChangePass checkLink(String link) throws DAOException {
        String query = "SELECT utente_id, link, scadenza\n" +
                       "FROM LINK \n" +
                       "WHERE link = ? and scadenza > current_timestamp";
        LinkChangePass daRitornare = null;
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setString(1, link);
            try (ResultSet rs = stm.executeQuery()) {
                
                while (rs.next()) {
                    
                    daRitornare = new LinkChangePass();
                    daRitornare.setUtenteId(rs.getInt("utente_id"));
                    daRitornare.setLink(rs.getString("link"));
                    daRitornare.setDataScadenza(rs.getTimestamp("scadenza"));
                    
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        query = "DELETE \n" +
                "FROM LINK \n" +
                "WHERE current_timestamp > scadenza";
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            
            stm.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Impossibile eliminare il link", ex);
        }
        
        return daRitornare; 
    }
    
    @Override
    public void deleteLink(int utenteId) throws DAOException {
        String query = "DELETE \n" +
                       "FROM LINK \n" +
                       "WHERE utente_id = ?";
        try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, utenteId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Impossibile eliminare il link", ex);
        }
    }
}
