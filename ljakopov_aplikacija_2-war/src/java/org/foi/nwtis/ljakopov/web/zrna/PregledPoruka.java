/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.web.kontrole.Izbornik;
import org.foi.nwtis.ljakopov.web.podaci.Poruka;
import org.foi.nwtis.ljakopov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Lovro
 */
@Named(value = "pregledPoruka")
@SessionScoped
public class PregledPoruka implements Serializable {

    private ArrayList<Izbornik> mape = new ArrayList<>();
    private ArrayList<Poruka> poruke = new ArrayList<>();
    Folder folder = null;
    private String odabranaMapa = "INBOX";

    /**
     * Creates a new instance of PregledPoruka
     */
    public PregledPoruka() {
        preuzmiMape();
        preuzmiPoruke();
    }

    public ArrayList<Izbornik> getMape() {
        return mape;
    }

    public void setMape(ArrayList<Izbornik> mape) {
        this.mape = mape;
    }

    public ArrayList<Poruka> getPoruke() {
        return poruke;
    }

    public void setPoruke(ArrayList<Poruka> poruke) {
        this.poruke = poruke;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public void preuzmiMape() {
        ServletContext sc = (ServletContext) SlusacAplikacije.getContext();
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        java.util.Properties properties = System.getProperties();
        properties.put("mail.smtp.host", konf.dajPostavku("mail.server"));
        Session session = Session.getInstance(properties, null);
        Store store;

        try {
            store = session.getStore("imap");
            store.connect(konf.dajPostavku("mail.server"), konf.dajPostavku("mail.usernameThread"), konf.dajPostavku("mail.passwordThread"));
            Folder[] f = store.getDefaultFolder().list();
            for (Folder fd : f) {
                mape.add(new Izbornik(fd.getName(), Integer.toString(fd.getMessageCount())));
            }

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preuzmiPoruke() {
        ServletContext sc = (ServletContext) SlusacAplikacije.getContext();
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        poruke.clear();
        folder = null;

        java.util.Properties properties = System.getProperties();
        properties.put("mail.smtp.host", konf.dajPostavku("mail.server"));
        Session session = Session.getInstance(properties, null);
        System.out.println("OVO JE ISPIS ZA PROMJENU MAPE: " + this.odabranaMapa);

        Store store;
        try {
            store = session.getStore("imap");
            store.connect(konf.dajPostavku("mail.server"), konf.dajPostavku("mail.usernameThread"), konf.dajPostavku("mail.passwordThread"));
            folder = store.getFolder(this.odabranaMapa);
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (int i = messages.length; i > 0; i--) {
                try {
                    System.out.println("OVO JE SAMO ISPIS");
                    poruke.add(new Poruka(String.valueOf(i), messages[i - 1].getSentDate(), messages[i - 1].getReceivedDate(), Arrays.toString(messages[i - 1].getFrom()), messages[i - 1].getSubject(), messages[i - 1].getContent().toString(), "0"));

                } catch (IOException ex) {
                    Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String promjenaMape() {
        this.preuzmiPoruke();
        return "";
    }

    public void obrisiPoruke() {
        ServletContext sc = (ServletContext) SlusacAplikacije.getContext();
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");

        java.util.Properties properties = System.getProperties();
        properties.put("mail.smtp.host", konf.dajPostavku("mail.server"));
        Session session = Session.getInstance(properties, null);

        Store store;
        try {
            store = session.getStore("imap");
            store.connect(konf.dajPostavku("mail.server"), konf.dajPostavku("mail.usernameThread"), konf.dajPostavku("mail.passwordThread"));
            folder = store.getFolder(this.odabranaMapa);
            folder.open(Folder.READ_WRITE);           
            Message[] messages = folder.getMessages();
            
            Flags flag = new Flags(Flags.Flag.DELETED);
            folder.setFlags(messages, flag, true);
            
            folder.close(true);
            store.close();
            
            preuzmiPoruke();
            preuzmiMape();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
