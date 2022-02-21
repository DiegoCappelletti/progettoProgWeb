/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.filters;

import it.serviziosanitario.persistence.dao.MedicoDAO;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.Medico;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alessio
 */
public class MedicoProfiloFilter implements Filter {
    
    private static final boolean DEBUG = false;

    private FilterConfig filterConfig = null;
    
    public MedicoProfiloFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("MedicoProfiloFilter:DoBeforeProcessing");
        }

        DAOFactory daoFactory = (DAOFactory) request.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new RuntimeException(new ServletException("Impossibile recuperare il dao factory"));
        }
        
        UtenteDAO utenteDao = null;
        try {
            utenteDao = daoFactory.getDAO(UtenteDAO.class);
            request.setAttribute("utenteDao", utenteDao);
        } catch (DAOFactoryException ex) {
            throw new RuntimeException(new ServletException("Impossibile recuperare il dao factory per l'utente", ex));
        }
        
        MedicoDAO medicoDao = null;
        try {
            medicoDao = daoFactory.getDAO(MedicoDAO.class);
            request.setAttribute("medicoDao", medicoDao);
        } catch (DAOFactoryException ex) {
            throw new RuntimeException(new ServletException("Impossibile recuperare il dao factory per il medico", ex));
        }
        
        String contextPath = request.getServletContext().getContextPath();
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        request.setAttribute("contextPath", contextPath);
        
        Integer utenteId = null;
        Utente utente = null;
        
        HttpSession sessione = ((HttpServletRequest) request).getSession(false);
        if (sessione != null) {
            if (DEBUG) System.out.println("MedicoProfiloFilter:        sessione not null.");
            utente = (Utente) sessione.getAttribute("utente");
        }
        if (utente != null) {
            if (DEBUG) System.out.println("MedicoProfiloFilter:        utente not null.");
            utenteId = utente.getUtenteId();
        }
        
        if (utenteId == null) {
            if (DEBUG) System.out.println("MedicoProfiloFilter:        utenteId null.");
            if (!response.isCommitted()) {
                ((HttpServletResponse) response).sendRedirect(((HttpServletResponse) response).encodeRedirectURL(contextPath + "/login.html"));
            }
        }

        try {
            if (utente == null) {
                if (DEBUG) System.out.println("MedicoProfiloFilter:        utente null.");
                utente = utenteDao.getByPrimaryKey(utenteId);
            }
        } catch (DAOException ex) {
            throw new RuntimeException(new ServletException("Impossibile recuperare l'utente", ex));
        }
        request.setAttribute("utente", utente);

        
        try {
            Medico medico = medicoDao.getByPrimaryKey(utenteId);
            if(medico == null) {
                ((HttpServletResponse) response).sendRedirect(((HttpServletResponse) response).encodeRedirectURL(contextPath + "/login.html"));
            }
            sessione.setAttribute("medico", medico);
        } catch (DAOException ex) {
            throw new RuntimeException(new ServletException("Impossibile recuperate il medico per il profilo", ex));
        }
        if (response.isCommitted()) {
            request.getServletContext().log("medico/profilo.html is already committed");
        }
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("MedicoProfiloFilter:DoAfterProcessing");
        }
        
        
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (DEBUG) {
            log("MedicoProfiloFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }
        
        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {                
                log("MedicoProfiloFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("MedicoProfiloFilter()");
        }
        StringBuffer sb = new StringBuffer("MedicoProfiloFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
