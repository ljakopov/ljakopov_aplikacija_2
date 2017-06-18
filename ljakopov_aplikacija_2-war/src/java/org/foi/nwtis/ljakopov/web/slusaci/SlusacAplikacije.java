/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.ljakopov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.ljakopov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.ljakopov.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.ljakopov.zrna.SingSessionBean;

/**
 * Web application lifecycle listener.
 *
 * @author Lovro
 */
public class SlusacAplikacije implements ServletContextListener {

    public static ServletContext context = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF")
                + File.separator
                + context.getInitParameter("konfiguracija");

        BP_Konfiguracija bp_konf = new BP_Konfiguracija(datoteka);
        context.setAttribute("BP_Konfig", bp_konf);
        System.out.println("Učitana konfiguacija");

        Konfiguracija konf = null;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            
            SingSessionBean.intervalDretve = Integer.parseInt(konf.dajPostavku("mail.timeSecThread"));
            SingSessionBean.server = konf.dajPostavku("mail.server");
            SingSessionBean.port = Integer.parseInt(konf.dajPostavku("mail.port"));
            SingSessionBean.korisnickoIme = konf.dajPostavku("mail.usernameThread");
            SingSessionBean.lozinka = konf.dajPostavku("mail.passwordThread");
            SingSessionBean.nwtisPoruke = konf.dajPostavku("mail.folderNWTiS");
            SingSessionBean.neNwtisPoruke = konf.dajPostavku("mail.folderOther");

            
            context.setAttribute("Mail_Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static ServletContext getContext() {
        return context;
    }
}
