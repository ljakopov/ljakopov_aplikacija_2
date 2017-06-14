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
public class MeteoRest {

    public static String preuzmiSveUredjaje() {
        MeteoRESTResourceContainer_JerseyClient meteoRESTResourceContainer_JerseyClient = new MeteoRESTResourceContainer_JerseyClient();
        return meteoRESTResourceContainer_JerseyClient.getJson();
    }
    
    public static String dodajUredjaj(Object jsObject){
        MeteoRESTResourceContainer_JerseyClient meteoRESTResourceContainer_JerseyClient = new MeteoRESTResourceContainer_JerseyClient();
        return meteoRESTResourceContainer_JerseyClient.postJson(jsObject);
    }

    static class MeteoRESTResourceContainer_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/ljakopov_aplikacija_1/webresources";

        public MeteoRESTResourceContainer_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("meteoRESTs");
        }

        public String postJson(Object requestEntity) throws ClientErrorException {
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
