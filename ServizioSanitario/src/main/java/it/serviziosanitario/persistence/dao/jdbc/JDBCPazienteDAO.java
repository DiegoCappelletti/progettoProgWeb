/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao.jdbc;

import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.entities.Città;
import it.serviziosanitario.persistence.entities.Esame;
import it.serviziosanitario.persistence.entities.Farmaco;
import it.serviziosanitario.persistence.entities.Foto;
import it.serviziosanitario.persistence.entities.Medico;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Provincia;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Specializzazione;
import it.serviziosanitario.persistence.entities.Ssp;
import it.serviziosanitario.persistence.entities.TipoEsame;
import it.serviziosanitario.persistence.entities.TipoVisita;
import it.serviziosanitario.persistence.entities.Visita;
import it.serviziosanitario.services.CodiciFiscaliService.CodiceFiscale;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loghi
 */
public class JDBCPazienteDAO extends JDBCDAO<Paziente, Integer> implements PazienteDAO {

    public JDBCPazienteDAO(Connection con) {
        super(con);
    }
    
    
    @Override
    public Long getCount() throws DAOException {
        try (Statement stmt = CON.createStatement()) {
            ResultSet counter = stmt.executeQuery("SELECT COUNT(*) FROM pazienti");
            if (counter.next()) {
                return counter.getLong(1);
            }

        } catch (SQLException ex) {
            throw new DAOException("Impossible to count patients", ex);
        }

        return 0L;
    }

    
    @Override
    public Paziente getByPrimaryKey(Integer primaryKey) throws DAOException {
        if(primaryKey==null){
            throw new DAOException("primaryKey is null");
        }
        Paziente paziente = new Paziente();
        String query = "SELECT pazienti.paziente_id as p_id , pazienti.nome as p_nome, pazienti.cognome as p_cognome, pazienti.codice_fiscale as p_codice_fiscale, pazienti.sesso as p_sesso, pazienti.indirizzo as p_indirizzo,\n" +
                "		citta.citta_id as cp_citta_id, citta.citta as cp_nome, province.provincia as pr_nome, province.provincia_id as pr_id, data_nascita, luogo_nascita,\n" +
                "		pazienti.medico_id as p_medico_id, utenti.email as p_email, data_ultima_visita, data_ultima_ricetta\n" +
                "FROM pazienti\n" +
                "JOIN citta \n" +
                "ON pazienti.citta_id = citta.citta_id\n" +
                "JOIN province\n" +
                "ON pazienti.provincia_id = province.provincia_id\n" +
                "JOIN utenti\n"+
                "ON paziente_id = utenti.utente_id\n"+
                "WHERE pazienti.paziente_id = ?";
        
        //recupera tutto tranne il medico e le foto
        int tmp_medic_id = -1; //id del medico da usare in un altra query
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("ERRORE: Il DAO ha ritornato piu pazienti");
                    }
                    
                    tmp_medic_id = rs.getInt("p_medico_id");
                    //System.out.println(tmp_medic_id);
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("data_nascita"));
                    paziente.setLuogoNascita(rs.getString("luogo_nascita"));
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setEmail(rs.getString("p_email"));
                    paziente.setDataUltimaRicetta(rs.getDate("data_ultima_ricetta"));
                    paziente.setDataUltimaVisita(rs.getDate("data_ultima_visita"));
                    Città città = new Città();
                    città.setCittàId(rs.getInt("cp_citta_id"));
                    città.setNome(rs.getString("cp_nome"));
                    paziente.setCittà_id(città);
                    
                    Provincia provincia = new Provincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    provincia.setProvinciaId(rs.getInt("pr_id"));
                    paziente.setProvincia(provincia);
                    città.setProvincia(provincia);
                    
                }
                //Se il paziente non è stato trovato, si ferma e ritorna un paziente null
                if(count == 0){
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        //Recupera il medico con tutti i suoi campi
        
        if(tmp_medic_id==-1){
            throw new DAOException("Errore nel recupero del medico");
        }
        
        query = "SELECT medici.nome as m_nome, medici.cognome as m_cognome , medici.codice_fiscale as m_codice_fiscale,\n"
                + " citta.citta_id as c_citta_id, citta.citta as c_nome, province.provincia_id as pr_id, province.provincia as pr_nome, S.spec_id as spec_id, S.spec as s_nome\n" +
                "FROM medici\n" +
                "JOIN citta \n" +
                "ON medici.citta_id = citta.citta_id\n" +
                "Join specializzazioni as S\n"+
                "ON medici.spec_id = S.spec_id\n"+
                "JOIN province\n" +
                "ON medici.provincia_id = province.provincia_id\n" +
                "WHERE medico_id = ?";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, tmp_medic_id);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("ERRORE: Il DAO ha ritornato piu di un medico");
                    }
                    Medico medico = new Medico();
                    medico.setUtenteId(tmp_medic_id);
                    medico.setNome(rs.getString("m_nome"));
                    medico.setCognome(rs.getString("m_cognome"));
                    medico.setCodiceFiscale(rs.getString("m_codice_fiscale"));
                    
                    Specializzazione specializzazione = new Specializzazione();
                    specializzazione.setSpecId(rs.getInt("spec_id"));
                    specializzazione.setNome(rs.getString("s_nome"));
                    
                    medico.setSpecializzazione(specializzazione);
                    
                    Città città = new Città();
                    città.setCittàId(rs.getInt("c_citta_id"));
                    città.setNome(rs.getString("c_nome"));
                    medico.setCittà(città);
                    
                    Provincia provincia = new Provincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    medico.setProvinciaId(provincia);
                    
                    paziente.setMedico(medico);
                    
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare il medico del paziente "+paziente.getNome()+" "+paziente.getCognome(), ex);
        }
        
        //Recupera la lista di foto del paziente
        //In testa alla lista c'è la foto piu recente
        query = "select F.foto_id as foto_id, F.path as path, F.data as data, F.paziente_id as p_id\n" +
            "from pazienti\n" +
            "JOIN foto as F\n" +
            "ON pazienti.paziente_id = F.paziente_id\n" +
            "WHERE pazienti.paziente_id = ?\n" +
            "ORDER BY data DESC";
        List<Foto> lista_foto = new ArrayList();
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, paziente.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Foto foto = new Foto();
                    foto.setFotoId(rs.getInt("foto_id"));
                    foto.setPath(rs.getString("path"));
                    foto.setData(rs.getDate("data"));
                    foto.setPazienteId(rs.getInt("p_id"));
                    lista_foto.add(foto);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare le foto del paziente "+paziente.getNome()+" "+paziente.getCognome(), ex);
        }
        paziente.setFoto(lista_foto);
        
        return paziente;
    }


    
    @Override
    public List<Paziente> getAll() throws DAOException {
        List<Paziente> lista_pazienti = new ArrayList<Paziente>();
        
        
            
         String query = "SELECT paziente_id , pazienti.nome as p_nome, pazienti.cognome as p_cognome, pazienti.codice_fiscale as p_codice_fiscale, pazienti.sesso as p_sesso, pazienti.indirizzo as p_indirizzo,\n" +
            "		citta.citta_id as cp_citta_id, citta.citta as cp_nome, province.provincia as pr_nome, province.provincia_id as pr_id, data_nascita, luogo_nascita,\n" +
            "		medici.medico_id as m_id, medici.nome as m_nome, medici.cognome as m_cognome, medici.codice_fiscale as m_codice_fiscale\n" +
            "FROM pazienti\n" +
            "JOIN citta \n" +
            "ON pazienti.citta_id = citta.citta_id\n" +
            "JOIN province\n" +
            "ON pazienti.provincia_id = province.provincia_id\n" +
            "JOIN medici\n" +
            "ON pazienti.medico_id = medici.medico_id";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    Paziente paziente = new Paziente();
                    
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("data_nascita"));
                    paziente.setLuogoNascita(rs.getString("luogo_nascita"));
                    paziente.setUtenteId(rs.getInt("paziente_id"));
                    Città città = new Città();
                    città.setCittàId(rs.getInt("cp_citta_id"));
                    città.setNome(rs.getString("cp_nome"));
                    paziente.setCittà_id(città);
                    
                    Provincia provincia = new Provincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    provincia.setProvinciaId(rs.getInt("pr_id"));
                    paziente.setProvincia(provincia);
                    città.setProvincia(provincia);
                
                    Medico medico = new Medico();
                    medico.setUtenteId(rs.getInt("m_id"));
                    medico.setNome(rs.getString("m_nome"));
                    medico.setCognome(rs.getString("m_cognome"));
                    medico.setCodiceFiscale(rs.getString("m_codice_fiscale"));
                    paziente.setMedico(medico);

                    lista_pazienti.add(paziente);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible ritornare la lista dei pazienti", ex);
        }

        return lista_pazienti;
    }
    
    @Override
    public List<Paziente> getPazientiByMedic(Medico medico) throws DAOException {
        int medico_id = medico.getUtenteId();
        List<Paziente> lista_pazienti = new ArrayList<Paziente>();
        
        
            
            String query =  "SELECT paziente_id , pazienti.nome as p_nome, pazienti.cognome as p_cognome, pazienti.codice_fiscale as p_codice_fiscale, pazienti.sesso as p_sesso, pazienti.indirizzo as p_indirizzo,\n" +
            "		citta.citta_id as cp_citta_id, citta.citta as cp_nome, province.provincia as pr_nome, province.provincia_id as pr_id, data_nascita, luogo_nascita,\n" +
            "		pazienti.medico_id, utenti.email as p_email\n" +
            "FROM pazienti\n" +
            "JOIN medici\n" +
            "ON pazienti.medico_id = medici.medico_id\n" +
            "JOIN citta \n" +
            "ON pazienti.citta_id = citta.citta_id\n" +
            "JOIN province\n" +
            "ON pazienti.provincia_id = province.provincia_id\n" +
            "JOIN utenti\n"+
            "ON paziente_id = utenti.utente_id\n"+
            "WHERE medici.medico_id = ?";
            try (PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, medico.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    Paziente paziente = new Paziente();
                    
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("data_nascita"));
                    paziente.setLuogoNascita(rs.getString("luogo_nascita"));
                    paziente.setUtenteId(rs.getInt("paziente_id"));
                    paziente.setEmail(rs.getString("p_email"));
                    Città città = new Città();
                    città.setCittàId(rs.getInt("cp_citta_id"));
                    città.setNome(rs.getString("cp_nome"));
                    paziente.setCittà_id(città);
                    
                    Provincia provincia = new Provincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    provincia.setProvinciaId(rs.getInt("pr_id"));
                    paziente.setProvincia(provincia);
                    città.setProvincia(provincia);
                
                    paziente.setMedico(medico);
                    
                    
                    
                    
                    
                    lista_pazienti.add(paziente);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible ritornare la lista dei pazienti", ex);
        }
        return lista_pazienti;
    }

    @Override
    public List<Medico> getByProvincia(Paziente paziente_corrente) throws DAOException {
        List<Medico> lista_medici = new ArrayList<Medico>();
        
        
            
         String query = "SELECT medico_id, nome, cognome, indirizzo, codice_fiscale, citta.citta_id as c_id, email,\n" +
                    "		citta.citta as c_nome, province.provincia_id as pr_id, province.provincia as pr_nome, S.spec_id as spec_id, S.spec as s_nome\n" +
                    "FROM citta\n" +
                    "JOIN medici\n" +
                    "ON medici.citta_id = citta.citta_id\n" +
                    "JOIN province \n" +
                    "ON medici.provincia_id = province.provincia_id\n" +
                    "JOIN utenti\n" +
                    "ON medici.medico_id = utenti.utente_id\n" +
                    "JOIN specializzazioni as S\n"+
                    "ON medici.spec_id = S.spec_id\n"+
                    "WHERE province.provincia_id = ? AND medici.codice_fiscale != ? AND S.spec_id = 0";
        
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, paziente_corrente.getProvincia().getProvinciaId());
            stm.setString(2, paziente_corrente.getCodiceFiscale());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Medico medico = new Medico();
                    medico.setUtenteId(rs.getInt("medico_id"));
                    medico.setNome(rs.getString("nome"));
                    medico.setCognome(rs.getString("cognome"));
                    medico.setCodiceFiscale(rs.getString("codice_fiscale"));
                    medico.setEmail(rs.getString("email"));
                    medico.setIndirizzo(rs.getString("indirizzo"));
                    Città città = new Città();
                    città.setCittàId(rs.getInt("c_id"));
                    città.setNome(rs.getString("c_nome"));
                    
                    Specializzazione specializzazione = new Specializzazione();
                    specializzazione.setSpecId(rs.getInt("spec_id"));
                    specializzazione.setNome(rs.getString("s_nome"));
                    
                    medico.setSpecializzazione(specializzazione);
                    
                    Provincia provincia = paziente_corrente.getProvincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    
                    città.setProvincia(provincia);
                    medico.setProvinciaId(provincia);
                    medico.setCittà(città);
                    
                    lista_medici.add(medico);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible ritornare la lista dei pazienti", ex);
        }

        return lista_medici;
    }

    @Override
    public Visita getVisitaByPrimaryKey(int primaryKey) throws DAOException {
        Visita visita = new Visita();
        String query = "SELECT p.nome as p_nome, p.cognome as p_cognome, p.paziente_id as p_id, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo, p.data_nascita as p_data_nascita,\n" +
                        "        p.luogo_nascita as p_luogo_nascita, m1.nome as m1_nome, m1.cognome as m1_cognome, m2.nome as m2_nome, m2.cognome as m2_cognome, tipo_visita_id, tv.nome as tv_nome, costo, tv.spec_id as spec_id,\n" +
                        "        data_erogazione, data_visita, eseguita, risultati, visita_id\n" +
                        "FROM visite\n" +
                        "JOIN pazienti as p\n" +
                        "ON visite.paziente_id = p.paziente_id\n" +
                        "JOIN medici as m1\n" +
                        "ON medico_base_id = m1.medico_id\n" +
                        "LEFT JOIN medici as m2\n" +
                        "ON medico_visita_id = m2.medico_id\n" +
                        "JOIN tipi_visita as tv\n" +
                        "ON tipo = tv.tipo_visita_id\n" +
                        "WHERE visite.visita_id = ?\n" +
                        "ORDER BY eseguita";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("Piu di una visita con la stessa primrayKEy");
                    }
                    
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Medico specialista = new Medico();
                    specialista.setNome(rs.getString("m2_nome"));
                    specialista.setCognome(rs.getString("m2_cognome"));
                    
                    TipoVisita t_visita = new TipoVisita();
                    t_visita.setNome(rs.getString("tv_nome"));
                    t_visita.setCosto(rs.getInt("costo"));
                    
                    visita.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    visita.setDataVisita(rs.getTimestamp("data_visita"));
                    visita.setEseguita(rs.getBoolean("eseguita"));
                    visita.setRisultati(rs.getString("risultati"));
                    
                    visita.setPaziente(paziente);
                    visita.setMedicoDiBase(base);
                    visita.setMedicoSpecialista(specialista);
                    visita.setTipoVisita(t_visita);
                    visita.setVisitaId(rs.getInt("visita_id"));
                    
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return visita;
    }

    @Override
    public List<Visita> getAllVisiteByPaziente(Paziente p) throws DAOException {
        List<Visita> lista_visite = new ArrayList();
        String query = "SELECT p.nome as p_nome, p.cognome as p_cognome, p.paziente_id as p_id, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo, p.data_nascita as p_data_nascita,\n" +
                        "        p.luogo_nascita as p_luogo_nascita, m1.nome as m1_nome, m1.cognome as m1_cognome, m2.nome as m2_nome, m2.cognome as m2_cognome, tipo_visita_id, tv.nome as tv_nome, costo, tv.spec_id as spec_id,\n" +
                        "        data_erogazione, data_visita, eseguita, risultati, visita_id\n" +
                        "FROM pazienti as p\n" +
                        "JOIN visite\n" +
                        "ON visite.paziente_id = p.paziente_id\n" +
                        "JOIN medici as m1\n" +
                        "ON medico_base_id = m1.medico_id\n" +
                        "LEFT JOIN medici as m2\n" +
                        "ON medico_visita_id = m2.medico_id\n" +
                        "JOIN tipi_visita as tv\n" +
                        "ON tipo = tv.tipo_visita_id\n" +
                        "WHERE p.paziente_id = ?\n" +
                        "ORDER BY data_visita DESC";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, p.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    
                    Visita visita = new Visita();
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Medico specialista = new Medico();
                    specialista.setNome(rs.getString("m2_nome"));
                    specialista.setCognome(rs.getString("m2_cognome"));
                    
                    TipoVisita t_visita = new TipoVisita();
                    t_visita.setNome(rs.getString("tv_nome"));
                    t_visita.setCosto(rs.getInt("costo"));
                    
                    visita.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    visita.setDataVisita(rs.getTimestamp("data_visita"));
                    visita.setEseguita(rs.getBoolean("eseguita"));
                    visita.setRisultati(rs.getString("risultati"));
                    
                    visita.setPaziente(paziente);
                    visita.setMedicoDiBase(base);
                    visita.setMedicoSpecialista(specialista);
                    visita.setTipoVisita(t_visita);
                    visita.setVisitaId(rs.getInt("visita_id"));
                    lista_visite.add(visita);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return lista_visite;
    }

    @Override
    public List<Visita> getEVisiteByPaziente(Paziente p) throws DAOException {
        List<Visita> lista_visite = new ArrayList();
        String query = "SELECT p.nome as p_nome, p.cognome as p_cognome, p.paziente_id as p_id, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo, p.data_nascita as p_data_nascita,\n" +
                        "        p.luogo_nascita as p_luogo_nascita, m1.nome as m1_nome, m1.cognome as m1_cognome, m2.nome as m2_nome, m2.cognome as m2_cognome, tipo_visita_id, tv.nome as tv_nome, costo, tv.spec_id as spec_id,\n" +
                        "        data_erogazione, data_visita, eseguita, risultati, visita_id\n" +
                        "FROM pazienti as p\n" +
                        "JOIN visite\n" +
                        "ON visite.paziente_id = p.paziente_id\n" +
                        "JOIN medici as m1\n" +
                        "ON medico_base_id = m1.medico_id\n" +
                        "LEFT JOIN medici as m2\n" +
                        "ON medico_visita_id = m2.medico_id\n" +
                        "JOIN tipi_visita as tv\n" +
                        "ON tipo = tv.tipo_visita_id\n" +
                        "WHERE p.paziente_id = ? AND eseguita = true\n" +
                        "ORDER BY eseguita";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, p.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    
                    Visita visita = new Visita();
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Medico specialista = new Medico();
                    specialista.setNome(rs.getString("m2_nome"));
                    specialista.setCognome(rs.getString("m2_cognome"));
                    
                    TipoVisita t_visita = new TipoVisita();
                    t_visita.setNome(rs.getString("tv_nome"));
                    t_visita.setCosto(rs.getInt("costo"));
                    
                    visita.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    visita.setDataVisita(rs.getTimestamp("data_visita"));
                    visita.setEseguita(rs.getBoolean("eseguita"));
                    visita.setRisultati(rs.getString("risultati"));
                    
                    visita.setPaziente(paziente);
                    visita.setMedicoDiBase(base);
                    visita.setMedicoSpecialista(specialista);
                    visita.setTipoVisita(t_visita);
                    visita.setVisitaId(rs.getInt("visita_id"));
                    lista_visite.add(visita);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return lista_visite;
    }

    @Override
    public List<Visita> getNEVisiteByPaziente(Paziente p) throws DAOException {
        List<Visita> lista_visite = new ArrayList();
        String query = "SELECT p.nome as p_nome, p.cognome as p_cognome, p.paziente_id as p_id, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo, p.data_nascita as p_data_nascita,\n" +
                        "        p.luogo_nascita as p_luogo_nascita, m1.nome as m1_nome, m1.cognome as m1_cognome, m2.nome as m2_nome, m2.cognome as m2_cognome, tipo_visita_id, tv.nome as tv_nome, costo, tv.spec_id as spec_id,\n" +
                        "        data_erogazione, data_visita, eseguita, risultati, visita_id\n" +
                        "FROM pazienti as p\n" +
                        "JOIN visite\n" +
                        "ON visite.paziente_id = p.paziente_id\n" +
                        "JOIN medici as m1\n" +
                        "ON medico_base_id = m1.medico_id\n" +
                        "LEFT JOIN medici as m2\n" +
                        "ON medico_visita_id = m2.medico_id\n" +
                        "JOIN tipi_visita as tv\n" +
                        "ON tipo = tv.tipo_visita_id\n" +
                        "WHERE p.paziente_id = ? AND eseguita = false\n" +
                        "ORDER BY eseguita";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, p.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    
                    Visita visita = new Visita();
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Medico specialista = new Medico();
                    specialista.setNome(rs.getString("m2_nome"));
                    specialista.setCognome(rs.getString("m2_cognome"));
                    
                    TipoVisita t_visita = new TipoVisita();
                    t_visita.setNome(rs.getString("tv_nome"));
                    t_visita.setCosto(rs.getInt("costo"));
                    
                    visita.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    visita.setDataVisita(rs.getTimestamp("data_visita"));
                    visita.setEseguita(rs.getBoolean("eseguita"));
                    visita.setRisultati(rs.getString("risultati"));
                    
                    visita.setPaziente(paziente);
                    visita.setMedicoDiBase(base);
                    visita.setMedicoSpecialista(specialista);
                    visita.setTipoVisita(t_visita);
                    visita.setVisitaId(rs.getInt("visita_id"));
                    lista_visite.add(visita);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        return lista_visite;
    }

    @Override
    public List<Esame> getAllEsamiByPaziente(Paziente p) throws DAOException {
        List<Esame> lista_esami = new ArrayList();
        String query="SELECT p.paziente_id as p_id, p.nome as p_nome, p.cognome as p_cognome, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo,\n" +
                    "		p.data_nascita as p_data_nascita, p.luogo_nascita as p_luogo_nascita,  m1.nome as m1_nome,m1.cognome as m1_cognome, costo, \n" +
                    "		m1.medico_id as m_id,esame_id, s.ssp_id as s_id, s.nome as s_nome, tipo, data_erogazione, data_esame, eseguito, risultati,s.provincia_id as s_provincia_id,\n" +
                    "		tipo_esame_id as te_id, te.nome as te_nome, e.esame_id as e_id, province.provincia as pr_nome, province.provincia_id as pr_id\n" +
                    "FROM pazienti as p\n" +
                    "JOIN esami as e\n" +
                    "ON e.paziente_id = p.paziente_id\n" +
                    "JOIN medici as m1\n" +
                    "ON e.medico_id = m1.medico_id\n" +
                    "LEFT JOIN ssp as s\n" +
                    "ON e.ssp_id = s.ssp_id\n" +
                    "JOIN tipi_esame as te\n" +
                    "ON tipo = te.tipo_esame_id\n" +
                    "LEFT JOIN province\n" +
                    "ON s.provincia_id = province.provincia_id\n" +
                    "WHERE p.paziente_id = ?\n" +
                    "ORDER BY data_esame DESC ";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, p.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    
                    Esame esame = new Esame();
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Ssp ssp = new Ssp();
                    ssp.setUtenteId(rs.getInt("s_id"));
                    ssp.setNome(rs.getString("s_nome"));
                    
                    
                    TipoEsame t_esame = new TipoEsame();
                    t_esame.setNome(rs.getString("te_nome"));
                    t_esame.setCosto(rs.getFloat("costo"));
                    t_esame.setTipiEsameId(rs.getInt("te_id"));
                    esame.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    esame.setDataEsame(rs.getTimestamp("data_esame"));
                    esame.setEseguito(rs.getBoolean("eseguito"));
                    esame.setRisultati(rs.getString("risultati"));
                    
                    esame.setPaziente(paziente);
                    esame.setMedico(base);
                    esame.setSsp(ssp);
                    esame.setTipoEsame(t_esame);
                    esame.setEsameId(rs.getInt("e_id"));
                    lista_esami.add(esame);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'utente", ex);
        }
        
        
        
        return lista_esami;
    
    }

    @Override
    public Esame getEsameByPrimaryKey(int primaryKey) throws DAOException {
        Esame esame = new Esame();
        String query="SELECT p.paziente_id as p_id, p.nome as p_nome, p.cognome as p_cognome, p.codice_fiscale as p_codice_fiscale, p.sesso as p_sesso, p.indirizzo as p_indirizzo,\n" +
                    "		p.data_nascita as p_data_nascita, p.luogo_nascita as p_luogo_nascita,  m1.nome as m1_nome,m1.cognome as m1_cognome, costo, \n" +
                    "		m1.medico_id as m_id,esame_id, s.ssp_id as s_id, s.nome as s_nome, tipo, data_erogazione, data_esame, eseguito, risultati,s.provincia_id as s_provincia_id,\n" +
                    "		tipo_esame_id as te_id, te.nome as te_nome, e.esame_id as e_id, province.provincia as pr_nome, province.provincia_id as pr_id\n" +
                    "FROM pazienti as p\n" +
                    "JOIN esami as e\n" +
                    "ON e.paziente_id = p.paziente_id\n" +
                    "JOIN medici as m1\n" +
                    "ON e.medico_id = m1.medico_id\n" +
                    "LEFT JOIN ssp as s\n" +
                    "ON e.ssp_id = s.ssp_id\n" +
                    "JOIN tipi_esame as te\n" +
                    "ON tipo = te.tipo_esame_id\n" +
                    "LEFT JOIN province\n" +
                    "ON s.provincia_id = province.provincia_id\n" +
                    "WHERE e.esame_id = ?\n" +
                    "ORDER BY eseguito";
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if(count>1){
                        throw new DAOException("Errore, piu di un esame con la stessa primaryKey");
                    }
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico base = new Medico();
                    base.setNome(rs.getString("m1_nome"));
                    base.setCognome(rs.getString("m1_cognome"));
                    
                    Ssp ssp = new Ssp();
                    ssp.setUtenteId(rs.getInt("s_id"));
                    ssp.setNome(rs.getString("s_nome"));
                    
                    
                    TipoEsame t_esame = new TipoEsame();
                    t_esame.setNome(rs.getString("te_nome"));
                    t_esame.setCosto(rs.getFloat("costo"));
                    t_esame.setTipiEsameId(rs.getInt("te_id"));
                    esame.setDataErogazione(rs.getTimestamp("data_erogazione"));
                    esame.setDataEsame(rs.getTimestamp("data_esame"));
                    esame.setEseguito(rs.getBoolean("eseguito"));
                    esame.setRisultati(rs.getString("risultati"));
                    
                    esame.setPaziente(paziente);
                    esame.setMedico(base);
                    esame.setSsp(ssp);
                    esame.setTipoEsame(t_esame);
                    esame.setEsameId(rs.getInt("e_id"));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile l'esame", ex);
        }
        
        return esame;
    }

    @Override
    public List<CodiceFiscale> getAllCodiciFiscali() throws DAOException {
        List<CodiceFiscale> codiciFiscali = new ArrayList<>();

        try (Statement stm = CON.createStatement()) {
            try (ResultSet rs = stm.executeQuery("SELECT codice_fiscale FROM pazienti ORDER BY codice_fiscale")) {
                int id = 0;
                while (rs.next()) {
                    CodiceFiscale cf = new CodiceFiscale(id, rs.getString("codice_fiscale"));
                    codiciFiscali.add(cf);
                    id++;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare la lista dei codici fiscali.", ex);
        }

        return codiciFiscali;

    }

    @Override
    public Ricetta getRicettaByPrimaryKey(int primaryKey) throws DAOException {
        Ricetta ricetta = new Ricetta();
        String query = "SELECT P.nome as p_nome, P.cognome as p_cognome, P.codice_fiscale as p_codice_fiscale, P.sesso as p_sesso, P.paziente_id as p_id, P.indirizzo as p_indirizzo, P.data_nascita as p_data_nascita,\n" +
                        "		P.luogo_nascita as p_luogo_nascita, M.nome as m_nome, M.cognome as m_cognome, M.medico_id as m_id, R.data_erogazione as r_data_erogazione,\n" +
                        "		R.ricetta_id as r_id, FA.nome as fa_nome, FA.farmaco_id as fa_id, FA.costo as fa_costo\n" +
                        "FROM RICETTE R\n" +
                        "JOIN PAZIENTI P\n" +
                        "ON R.paziente_id = P.paziente_id\n" +
                        "JOIN MEDICI M\n" +
                        "ON R.medico_base_id = M.medico_id\n" +
                        "JOIN FARMACI FA\n" +
                        "ON FA.farmaco_id = R.farmaco_id\n" +
                        "WHERE R.ricetta_id = ?";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("Piu di una ricetta con la stessa primrayKEy");
                    }
                    
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico medico = new Medico();
                    medico.setNome(rs.getString("m_nome"));
                    medico.setCognome(rs.getString("m_cognome"));
                    medico.setUtenteId(rs.getInt("m_id"));
                    
                    
                    
                    
                    Farmaco farmaco = new Farmaco();
                    farmaco.setNome(rs.getString("fa_nome"));
                    farmaco.setCosto(rs.getFloat("fa_costo"));
                    farmaco.setFarmacoId(rs.getInt("fa_id"));
                    
                    ricetta.setPaziente(paziente);
                    ricetta.setMedico(medico);
                    ricetta.setFarmaco(farmaco);
                    ricetta.setDataErogazione(rs.getTimestamp("r_data_erogazione"));
                    ricetta.setRicettaId(rs.getInt("r_id"));
                    

                    
                }
                
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare la ricetta", ex);
        }
        
        return ricetta;
    }

    @Override
    public List<Ricetta> getAllRicetteByPaziente(Paziente pazienteR) throws DAOException {
        List<Ricetta> lista = new ArrayList<Ricetta>();
        String query = "SELECT P.nome as p_nome, P.cognome as p_cognome, P.codice_fiscale as p_codice_fiscale, P.sesso as p_sesso, P.paziente_id as p_id, P.indirizzo as p_indirizzo, P.data_nascita as p_data_nascita,\n" +
                        "		P.luogo_nascita as p_luogo_nascita, M.nome as m_nome, M.cognome as m_cognome, M.medico_id as m_id, R.data_erogazione as r_data_erogazione,\n" +
                        "		R.ricetta_id as r_id, FA.nome as fa_nome, FA.farmaco_id as fa_id, FA.costo as fa_costo\n" +
                        "FROM RICETTE R\n" +
                        "JOIN PAZIENTI P\n" +
                        "ON R.paziente_id = P.paziente_id\n" +
                        "JOIN MEDICI M\n" +
                        "ON R.medico_base_id = M.medico_id\n" +
                        "JOIN FARMACI FA\n" +
                        "ON FA.farmaco_id = R.farmaco_id\n" +
                        "WHERE P.paziente_id = ?\n"+
                        "ORDER BY data_erogazione DESC";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, pazienteR.getUtenteId());
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    Ricetta ricetta = new Ricetta();
                    
                    Paziente paziente = new Paziente();
                    paziente.setUtenteId(rs.getInt("p_id"));
                    paziente.setNome(rs.getString("p_nome"));
                    paziente.setCognome(rs.getString("p_cognome"));
                    paziente.setCodiceFiscale(rs.getString("p_codice_fiscale"));
                    paziente.setSesso(rs.getString("p_sesso").charAt(0));
                    paziente.setIndirizzo(rs.getString("p_indirizzo"));
                    paziente.setDataNascita(rs.getDate("p_data_nascita"));
                    paziente.setLuogoNascita(rs.getString("p_luogo_nascita"));
                    
                    Medico medico = new Medico();
                    medico.setNome(rs.getString("m_nome"));
                    medico.setCognome(rs.getString("m_cognome"));
                    
                    
                    
                    Farmaco farmaco = new Farmaco();
                    farmaco.setNome(rs.getString("fa_nome"));
                    farmaco.setCosto(rs.getFloat("fa_costo"));
                    farmaco.setFarmacoId(rs.getInt("fa_id"));
                    
                    ricetta.setPaziente(paziente);
                    ricetta.setMedico(medico);
                    ricetta.setFarmaco(farmaco);
                    ricetta.setDataErogazione(rs.getTimestamp("r_data_erogazione"));
                    ricetta.setRicettaId(rs.getInt("r_id"));
                    
                    lista.add(ricetta);
                    
                }
                
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare la ricetta", ex);
        }
        
        return lista;
    }

    @Override
    public void insertFoto(Paziente paziente, String path) throws DAOException {
         String query = "INSERT INTO foto (paziente_id, path, data)\n" +
                         "VALUES (?, ?, current_date)";
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            ps.setInt(1, paziente.getUtenteId());
            ps.setString(2, path);
            int count = ps.executeUpdate();
            if (count == 0) {
                throw new DAOException("Foto non aggiunta!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile aggiungere la foto");
        }
    }

    @Override
    public void deleteFoto(int primaryKey) throws DAOException {
        String query = "delete from foto where paziente_id = ?";
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            ps.setInt(1, primaryKey);
            int count = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile cancellare la foto");
        }
    }

    @Override
    public boolean checkRicetta(int utenteId, int ricettaId) throws DAOException {
        String query = "select ricetta_id from ricette where paziente_id = ? and ricetta_id = ?";
        int count=0;
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, utenteId);
            stm.setInt(2, ricettaId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    count++; 
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile controllare la ricetta", ex);
        }
        
        if(count==0){
            return false;
        }else{
            return true;
        } 
    }

    @Override
    public void cambiaMedico(int pazienteId, int medicoId) throws DAOException {
        String query = "update pazienti set medico_id = ? where paziente_id = ?";
        int count = 0;
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            ps.setInt(1, medicoId);
            ps.setInt(2, pazienteId);
            count = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile cambiare medico");
        }
        if(count == 0){
            throw new DAOException("Impossibile cambiare medico");
        }
    }
    
}
