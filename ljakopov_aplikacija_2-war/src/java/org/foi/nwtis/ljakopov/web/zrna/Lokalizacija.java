/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.foi.nwtis.ljakopov.web.kontrole.Izbornik;

/**
 *
 * @author Lovro
 */
@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {

    private static final ArrayList<Izbornik> izbornikJezika = new ArrayList<>();

    private static String odabraniJezik;

    static {
        izbornikJezika.add(new Izbornik("hrvatski", "hr"));
        izbornikJezika.add(new Izbornik("engleski", "en"));
    }

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
    }

    public String getOdabraniJezik() {
        System.out.println("TU SMO");
        //FacesContext FC = FacesContext.getCurrentInstance();
        UIViewRoot UVIR = FacesContext.getCurrentInstance().getViewRoot();
        if (UVIR != null) {
            Locale lokalniJezik = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            odabraniJezik = lokalniJezik.getLanguage();
        }
        if(odabraniJezik==null){
            odabraniJezik = "hr";
        }
        System.out.println("ODABRANI JEZIK: " + odabraniJezik);
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
        Locale lokalniJezik = new Locale(odabraniJezik);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(lokalniJezik);
    }

    public ArrayList<Izbornik> getIzbornikJezika() {
        return izbornikJezika;
    }

    public String odaberiJezik() {
        setOdabraniJezik(odabraniJezik);
        return "korisnik";
    }

}
