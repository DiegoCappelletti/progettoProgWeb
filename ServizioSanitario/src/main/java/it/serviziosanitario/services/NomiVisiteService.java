package it.serviziosanitario.services;

/**
 *
 * @author Alessio
 */

import it.serviziosanitario.persistence.dao.MedicoDAO;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 */
@Path("nomi-visite")
public class NomiVisiteService {

    @Context
    private UriInfo context;
    
    @Context
    private HttpServletRequest request;
    
    @Context
    private HttpServletResponse response;

    private ServletContext servletContext;

    
    private MedicoDAO medicoDao;
    
    /**
     * Creates a new instance of LanguagesService
     */
    public NomiVisiteService() {
    }
    
    @Context
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        if (servletContext != null) {
            DAOFactory daoFactory = (DAOFactory) servletContext.getAttribute("daoFactory");
            if (daoFactory == null) {
                throw new RuntimeException(new ServletException("Impossible to get dao factory for storage system"));
            }
            try {
                medicoDao = daoFactory.getDAO(MedicoDAO.class);
            } catch (DAOFactoryException ex) {
                throw new RuntimeException(new ServletException("Impossible to get dao factory", ex));
            }
        }
    }


    /**
     * Retrieves json representation of a collection of {@code LanguagesService}.<br>
     * In this version the term parameter is passed as query param.
     *
     * @param queryTerm search term passed as restful query parameter.
     * @return an instance of java.lang.String the json version of the results.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNomiVisite(@QueryParam("term") String queryTerm) {
        List<NomeVisita> nomiVisite = null;
        try {
            nomiVisite = medicoDao.getAllNomiVisite();
        } catch (DAOException ex) {
            throw new RuntimeException("Impossibile recuperare le visite", ex);
        }
        List<NomeVisita> results = new ArrayList<>();
        System.out.println(nomiVisite.get(2).text);
        if (queryTerm == null) {
            results.addAll(nomiVisite);
        } else {
            nomiVisite.stream().filter((NomeVisita cf) -> (cf.getText().toLowerCase().contains(queryTerm.toLowerCase()))).forEach(_item -> {
                results.add(_item);
            });
        }

        Gson gson = new Gson();
        return Response.ok(gson.toJson(new Results(results.toArray(new NomeVisita[0])))).header("Access-Control-Allow-Origin", "*").build();
    }

    public static class NomeVisita implements Serializable {

        private Integer id;
        private String text;

        public NomeVisita(Integer id, String text) {
            this.id = id;
            this.text = text;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private static class Results implements Serializable {

        private NomeVisita[] results;

        public Results(NomeVisita[] results) {
            this.results = results;
        }

        public NomeVisita[] getResults() {
            return results;
        }

        public void setResults(NomeVisita[] results) {
            this.results = results;
        }
    }
}

