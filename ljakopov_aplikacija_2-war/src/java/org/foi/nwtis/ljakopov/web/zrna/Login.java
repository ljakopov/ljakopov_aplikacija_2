/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.io.StringReader;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRestKorisnickoIme;

/**
 *
 * @author Lovro
 */
@Named(value = "login")
@RequestScoped
public class Login {

    /**
     * Creates a new instance of Login
     */
    public Login() {
    }

    private String korisnickoIme;
    private String lozinka;
    private String greska;

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getGreska() {
        return greska;
    }

    public void setGreska(String greska) {
        this.greska = greska;
    }
    
    private boolean provjeriUpisanePodatke(){
        return !(korisnickoIme.isEmpty() || lozinka.isEmpty());
    }
    public void logiranje(){
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle text = ResourceBundle.getBundle("org.foi.nwtis.ljakopov.i18n", context.getViewRoot().getLocale());
        if(provjeriUpisanePodatke()){
            UserRestKorisnickoIme userRestKorisnickoIme = new UserRestKorisnickoIme();
            String json = userRestKorisnickoIme.loginKorisnika(korisnickoIme);
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonArray array = reader.readArray();
            if(lozinka.equals(array.getJsonObject(0).getString("pass"))){
                System.out.println("USPJEŠNO STE LOGIRANNI");
                //TODO staviti session i dalje to odraditi
            }else{
                greska = text.getString("login_PrijavaNijeUspjela");
            }          
        }else{
            greska = text.getString("login_nisuUpisaniPassIKorisnickoIme");
        }
    }

}
