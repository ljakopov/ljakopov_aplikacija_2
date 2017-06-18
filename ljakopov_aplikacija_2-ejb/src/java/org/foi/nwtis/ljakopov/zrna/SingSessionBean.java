/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.zrna;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import org.foi.nwtis.ljakopov.web.dretve.ObradaPoruka;

/**
 *
 * @author Lovro
 */
@Startup
@Singleton
@LocalBean
public class SingSessionBean {

    public static int port;
    public static String server;
    public static int intervalDretve;
    public static String korisnickoIme;
    public static String lozinka;
    public static String nwtisPoruke;
    public static String neNwtisPoruke;

    ObradaPoruka obradaEmail;

    @PostConstruct
    void init() {
        System.out.println("SAMO POKRETANJE");
        obradaEmail = new ObradaPoruka();
        obradaEmail.start();
        //System.out.println("OVO JE: " + neNwtisPoruke);
    }

    @PreDestroy
    void destroy() {
        if (obradaEmail != null) {
            obradaEmail.interrupt();
        }
    }
}
