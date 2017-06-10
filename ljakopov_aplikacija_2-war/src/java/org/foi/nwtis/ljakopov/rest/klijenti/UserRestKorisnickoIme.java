/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.rest.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Lovro
 */
public class UserRestKorisnickoIme {
    
     public String loginKorisnika(String korisnickoIme){
       UserRESTResource_JerseyClient userRESTResource_JerseyClient = new UserRESTResource_JerseyClient(korisnickoIme);
       return userRESTResource_JerseyClient.getJson();
    }

    static class UserRESTResource_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/ljakopov_aplikacija_1/webresources";

        public UserRESTResource_JerseyClient(String korisnickoIme) {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            String resourcePath = java.text.MessageFormat.format("userREST/{0}", new Object[]{korisnickoIme});
            webTarget = client.target(BASE_URI).path(resourcePath);
        }

        public void setResourcePath(String korisnickoIme) {
            String resourcePath = java.text.MessageFormat.format("userREST/{0}", new Object[]{korisnickoIme});
            webTarget = client.target(BASE_URI).path(resourcePath);
        }

        public String putJson(Object requestEntity) throws ClientErrorException {
            return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public void delete() throws ClientErrorException {
            webTarget.request().delete();
        }

        public String getJson() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public void close() {
            client.close();
        }
    }
    
    
}
