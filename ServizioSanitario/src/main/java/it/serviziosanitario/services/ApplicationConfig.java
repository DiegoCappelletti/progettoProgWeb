/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.services;

/**
 *
 * @author Alessio
 */

import java.util.Set;
import javax.ws.rs.core.Application;


@javax.ws.rs.ApplicationPath("services")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(it.serviziosanitario.services.CodiciFiscaliService.class);
        resources.add(it.serviziosanitario.services.NomiEsamiService.class);
        resources.add(it.serviziosanitario.services.NomiFarmaciService.class);
        resources.add(it.serviziosanitario.services.NomiVisiteService.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }

}
