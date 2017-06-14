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
public class MeteoRestId {

    public static String dohvatiUredjaj(String id){
        MeteoRESTResource_JerseyClient meteoRESTResource_JerseyClient = new MeteoRESTResource_JerseyClient(id);
        return meteoRESTResource_JerseyClient.getJson();
    }
    
    public static String azurirajIoTUredjaj(String id, Object jsonObject){
        MeteoRESTResource_JerseyClient meteoRESTResource_JerseyClient = new MeteoRESTResource_JerseyClient(id);
        return meteoRESTResource_JerseyClient.putJson(jsonObject);
    }
    
    static class MeteoRESTResource_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/ljakopov_aplikacija_1/webresources";

        public MeteoRESTResource_JerseyClient(String id) {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            String resourcePath = java.text.MessageFormat.format("meteoRESTs/{0}", new Object[]{id});
            webTarget = client.target(BASE_URI).path(resourcePath);
        }

        public void setResourcePath(String id) {
            String resourcePath = java.text.MessageFormat.format("meteoRESTs/{0}", new Object[]{id});
            webTarget = client.target(BASE_URI).path(resourcePath);
        }

        public String putJson(Object requestEntity) throws ClientErrorException {
            return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
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
