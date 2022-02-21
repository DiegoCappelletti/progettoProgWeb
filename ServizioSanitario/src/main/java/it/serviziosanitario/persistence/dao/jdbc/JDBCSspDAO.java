/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.persistence.dao.jdbc;

import it.serviziosanitario.persistence.dao.SspDAO;
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
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loghi
 */
public class JDBCSspDAO extends JDBCDAO<Ssp ,Integer> implements SspDAO{

    public JDBCSspDAO(Connection con){
        super(con);
    }
    @Override
    public Long getCount() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Ssp getByPrimaryKey(Integer primaryKey) throws DAOException {
        Ssp ssp = new Ssp();
        ssp.setUtenteId(-1);
        String query = "SELECT ssp_id, ssp.nome as s_nome, indirizzo as s_indirizzo, province.provincia_id as pr_id, province.provincia as pr_nome\n" +
                        "FROM ssp\n" +
                        "JOIN province\n" +
                        "ON ssp.provincia_id = province.provincia_id\n" +
                        "WHERE ssp_id = ?";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    if(count>1){
                        throw new DAOException("Piu di un Ssp con la stessa primaryKey");
                    }
                    
                    Provincia provincia = new Provincia();
                    provincia.setNome(rs.getString("pr_nome"));
                    provincia.setProvinciaId(rs.getInt("pr_id"));
                    
                    ssp.setProvincia(provincia);
                    ssp.setUtenteId(rs.getInt("ssp_id"));
                    ssp.setNome(rs.getString("s_nome"));
                    ssp.setIndirizzo(rs.getString("s_indirizzo"));
                }
                if(count == 0){
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible ritornare il Ssp", ex);
        }
        
        if(ssp.getUtenteId() == -1){
            throw new DAOException("Ssp non trovato con la primaryKey specificata");
        }
        return ssp;
    }

    @Override
    public List<Ssp> getAll() throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paziente getPazienteByPrimaryKey(Integer primaryKey) throws DAOException {
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
        query = "select foto_id, path, data\n" +
            "from pazienti\n" +
            "LEFT JOIN foto\n" +
            "ON pazienti.paziente_id = foto.paziente_id\n" +
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
    public List<Paziente> getPazientiByAgeRange(int min_age, int max_age) throws Exception {
        if( min_age > max_age){
            throw new Exception("L'eta minima è piu grande di quella massima!"); //da controllare prima!
        }

        LocalDate low = LocalDate.now().minusYears(max_age+1); //anno corrente meno gli anni massimi +1  (finche non li ha compiuti)
        LocalDate high = LocalDate.now().minusYears(min_age); //anno corrente meno gli anni minimi
        
        //converto in sql date
        Date lower_date = java.sql.Date.valueOf(low);
        Date higher_date = java.sql.Date.valueOf(high);
        
        List<Paziente> lista = new ArrayList<Paziente>();
        
        String query = "SELECT paziente_id , pazienti.nome as p_nome, pazienti.cognome as p_cognome, pazienti.codice_fiscale as p_codice_fiscale, pazienti.sesso as p_sesso, pazienti.indirizzo as p_indirizzo,\n" +
            "		citta.citta_id as cp_citta_id, citta.citta as cp_nome, province.provincia as pr_nome, province.provincia_id as pr_id, data_nascita, luogo_nascita,\n" +
            "		pazienti.medico_id as m_id\n" +
            "FROM pazienti\n" +
            "JOIN citta \n" +
            "ON pazienti.citta_id = citta.citta_id\n" +
            "JOIN province\n" +
            "ON pazienti.provincia_id = province.provincia_id\n" +
            "WHERE pazienti.data_nascita < ? and pazienti.data_nascita >?";
        
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setDate(1,higher_date);
            stm.setDate(2,lower_date);
            
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
                    
                    lista.add(paziente);  
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Impossibile recuperare gli utenti", ex);
        }
        
        return lista;
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
                    "ORDER BY eseguito";
        
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
    public void InserisciEsame(Paziente paziente, Medico medico, TipoEsame tipo) throws DAOException {
        String query = "INSERT INTO esami (paziente_id, medico_id, ssp_id, tipo, data_erogazione, data_esame, eseguito, risultati)\n" +
                        "VALUES (?,?, null, ?, current_timestamp, null, false, 'ESAME NON ESEGUITO')";
        
        
        if (paziente.getUtenteId()==0){
            throw new DAOException("Il paziente non ha l'id utente");
        }
        
        if(medico.getUtenteId()==0){
            throw new DAOException("Il medico non ha l'id utente");
        }
        
        if(tipo.getTipiEsameId()==0){
            throw new DAOException("L'esame non ha un id");
        }
        
        
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            
            ps.setInt(1, paziente.getUtenteId());
            ps.setInt(2, medico.getUtenteId());
            ps.setInt(3, tipo.getTipiEsameId());
           
            
            int count = ps.executeUpdate();
            
            if (count == 0) {
                throw new DAOException("Esame non creato");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile creare l'esame");
        }
    }

    @Override
    public void EseguiEsame(Ssp ssp, Esame esame, String risultati) throws DAOException {
        String query = "UPDATE esami\n" +
                        "SET ssp_id = ?, data_esame = current_timestamp, eseguito = true, risultati = ?\n" +
                        "WHERE esame_id = ?";
        
        
        if(ssp.getUtenteId()==0){
            throw new DAOException("L'ssp non ha l'id utente");
        }
        
        if(esame.getEsameId()==0){
            throw new DAOException("L'esame non ha un id");
        }
        
        
        try (java.sql.PreparedStatement ps = CON.prepareStatement(query)) {
            
            ps.setInt(1, ssp.getUtenteId());
            ps.setString(2, risultati);
            ps.setInt(3, esame.getEsameId());
           
            
            int count = ps.executeUpdate();
            
            if (count == 0) {
                throw new DAOException("Esame non aggiornato: L'esame da eseguire non è stato trovato");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Impossibile aggiornare l'esame");
        }
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
                //se non è stato trovato nessun esame
                if(count == 0){
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossibile recuperare l'esame", ex);
        }
        
        return esame;
    }

    @Override
    public List<Esame> getEEsamiByPaziente(Paziente p) throws DAOException {
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
                    "WHERE p.paziente_id = ? AND eseguito=true\n" +
                    "ORDER BY eseguito";
        //System.out.println(query);
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
    public List<Esame> getNEsamiByPaziente(Paziente p) throws DAOException {
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
                    "WHERE p.paziente_id = ? AND eseguito=false\n" +
                    "ORDER BY eseguito";
        //System.out.println(query);
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
    public List<Paziente> getAllPazienti() throws DAOException {
        List<Paziente> lista_pazienti = new ArrayList<Paziente>();
        
        
            
            String query =  "SELECT paziente_id , data_ultima_visita, data_ultima_ricetta, pazienti.nome as p_nome, pazienti.cognome as p_cognome, pazienti.codice_fiscale as p_codice_fiscale, pazienti.sesso as p_sesso, pazienti.indirizzo as p_indirizzo,\n" +
            "		citta.citta_id as cp_citta_id, citta.citta as cp_nome, province.provincia as pr_nome, province.provincia_id as pr_id, data_nascita, luogo_nascita,\n" +
            "		pazienti.medico_id, utenti.email as p_email, medici.nome as m_nome, medici.cognome as m_cognome, medici.medico_id as m_id\n" +
            "FROM pazienti\n" +
            "JOIN medici\n" +
            "ON pazienti.medico_id = medici.medico_id\n" +
            "JOIN citta \n" +
            "ON pazienti.citta_id = citta.citta_id\n" +
            "JOIN province\n" +
            "ON pazienti.provincia_id = province.provincia_id\n" +
            "JOIN utenti\n"+
            "ON paziente_id = utenti.utente_id";
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
                
                    Medico medico = new Medico();
                    medico.setNome(rs.getString("m_nome"));
                    medico.setCognome(rs.getString("m_cognome"));
                    medico.setUtenteId(rs.getInt("m_id"));
                    paziente.setMedico(medico);
                    
                    
                    
                    
                    
                    lista_pazienti.add(paziente);
                }
            }
        } catch (SQLException ex) {
            //throw new SQLException("Impossible ritornare la lista dei pazienti", ex);
            ex.printStackTrace();
        }

        return lista_pazienti;
    }

    @Override
    public List<Ricetta> getAllRicetteByIdProvincia(int provinciaId) throws DAOException {
        List<Ricetta> lista = new ArrayList<Ricetta>();
        String query = "SELECT P.nome as p_nome, P.cognome as p_cognome, P.codice_fiscale as p_codice_fiscale, P.sesso as p_sesso, P.paziente_id as p_id, P.indirizzo as p_indirizzo, P.data_nascita as p_data_nascita,\n" +
                                "P.luogo_nascita as p_luogo_nascita, M.nome as m_nome, M.cognome as m_cognome, M.medico_id as m_id, R.data_erogazione as r_data_erogazione,\n" +
                                "R.ricetta_id as r_id, FA.nome as fa_nome, FA.farmaco_id as fa_id, FA.costo as fa_costo\n" +
                        "FROM RICETTE R\n" +
                        "JOIN PAZIENTI P\n" +
                        "ON R.paziente_id = P.paziente_id\n" +
                        "JOIN MEDICI M\n" +
                        "ON R.medico_base_id = M.medico_id\n" +
                        "JOIN FARMACI FA\n" +
                        "ON FA.farmaco_id = R.farmaco_id\n" +
                        "JOIN PROVINCE as PR\n" +
                        "ON PR.provincia_id = M.provincia_id\n" +
                        "WHERE PR.provincia_id = ?\n"+ 
                        "ORDER BY R.data_erogazione DESC";
        
        try (java.sql.PreparedStatement stm = CON.prepareStatement(query)) {
            stm.setInt(1, provinciaId);
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
                    medico.setUtenteId(rs.getInt("m_id"));
                    
                    paziente.setMedico(medico);
                    
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
    
    
}
