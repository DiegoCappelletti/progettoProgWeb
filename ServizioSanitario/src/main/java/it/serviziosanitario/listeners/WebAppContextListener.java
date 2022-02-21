
package it.serviziosanitario.listeners;

import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import it.unitn.disi.wp.commons.persistence.dao.factories.jdbc.JDBCDAOFactory;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class WebAppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        String rootPath = sce.getServletContext().getRealPath("/");
        int index = rootPath.indexOf("\\target");
        try {
            rootPath = rootPath.substring(0, index);
        } catch (StringIndexOutOfBoundsException ex) {
            throw new RuntimeException(ex);
        }
        sce.getServletContext().setInitParameter("rootPath", rootPath);
        
        String avatarsFolder = rootPath + "\\src\\main\\webapp\\images\\avatars";
        sce.getServletContext().setInitParameter("avatarsFolder", avatarsFolder);
        
        String dburl = "jdbc:derby:" + rootPath + "\\..\\SSdb.db";
        sce.getServletContext().setInitParameter("dburl", dburl);
        
        System.out.println("Root Path:      "+rootPath);
        System.out.println("Avatars Folder: "+avatarsFolder);
        System.out.println("Database Url:   "+dburl);
        
        //String dburl = sce.getServletContext().getInitParameter("dburl");
        try {
            JDBCDAOFactory.configure(dburl);
            DAOFactory daoFactory = JDBCDAOFactory.getInstance();
            sce.getServletContext().setAttribute("daoFactory", daoFactory);
        } catch (DAOFactoryException ex) {
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
        
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DAOFactory daoFactory = (DAOFactory) sce.getServletContext().getAttribute("daoFactory");
        if (daoFactory != null) {
            daoFactory.shutdown();
        }
        daoFactory = null;
    }
}
