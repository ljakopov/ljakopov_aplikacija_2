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
 * @author ljakopov
 */
public class UserRest {
    
    public String unesiKorisnika(Object jsonObject){
        UserRESTsResourceContainerClient userRESTsResourceContainerClient = new UserRESTsResourceContainerClient();
        return userRESTsResourceContainerClient.postJson(jsonObject);
    }

    static class UserRESTsResourceContainerClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/ljakopov_aplikacija_1/webresources";

        public UserRESTsResourceContainerClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("userREST");
        }

        public String postJson(Object requestEntity) throws ClientErrorException {
            System.out.println("ISPIS SA: " + requestEntity.toString());
            return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
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
