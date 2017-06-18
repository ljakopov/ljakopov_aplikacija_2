/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.ws.klijenti;

import org.foi.nwtis.ljakopov.ws.serveri.MeteoPodaci;

/**
 *
 * @author ljakopov
 */
public class MeteoWsKlijent {

    public static MeteoPodaci dajMeteoPodatkeZaUredjaj(java.lang.String username, java.lang.String pass, int id) {
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service service = new org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service();
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS port = service.getMeteoServiceWSPort();
        return port.dajMeteoPodatkeZaUredjaj(username, pass, id);
    }

    public static String dajAdresuNaTemeljuLatLon(java.lang.String username, java.lang.String pass, java.lang.String lat, java.lang.String lon) {
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service service = new org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service();
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS port = service.getMeteoServiceWSPort();
        return port.dajAdresuNaTemeljuLatLon(username, pass, lat, lon);
    }

    public static MeteoPodaci dajZadnjiMeteoPodatakZaUredjaj(java.lang.String username, java.lang.String pass, int id) {
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service service = new org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS_Service();
        org.foi.nwtis.ljakopov.ws.serveri.MeteoServiceWS port = service.getMeteoServiceWSPort();
        return port.dajZadnjiMeteoPodatakZaUredjaj(username, pass, id);
    }

}
