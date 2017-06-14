/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.io.StringReader;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRest;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRestKorisnickoIme;
import org.foi.nwtis.ljakopov.web.podaci.SesijaKorisnika;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author ljakopov
 */
@Named(value = "registracija")
@RequestScoped
public class Registracija {

    /**
     * Creates a new instance of Registracija
     */
    public Registracija() {
    }

    private String prezime;
    private String email;
    private String korisnickoIme;
    private String ponovljenaLozinka;
    private String lozinka;
    private String prikazOdgovora;

    private String prijavaKorisnickoIme;
    private String prijavaLozinka;
    private String greska;

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getPonovljenaLozinka() {
        return ponovljenaLozinka;
    }

    public void setPonovljenaLozinka(String ponovljenaLozinka) {
        this.ponovljenaLozinka = ponovljenaLozinka;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPrikazOdgovora() {
        return prikazOdgovora;
    }

    public void setPrikazOdgovora(String prikazOdgovora) {
        this.prikazOdgovora = prikazOdgovora;
    }

    public String getPrijavaKorisnickoIme() {
        return prijavaKorisnickoIme;
    }

    public void setPrijavaKorisnickoIme(String prijavaKorisnickoIme) {
        this.prijavaKorisnickoIme = prijavaKorisnickoIme;
    }

    public String getPrijavaLozinka() {
        return prijavaLozinka;
    }

    public void setPrijavaLozinka(String prijavaLozinka) {
        this.prijavaLozinka = prijavaLozinka;
    }

    public String getGreska() {
        return greska;
    }

    public void setGreska(String greska) {
        this.greska = greska;
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private boolean provjeraUpisaniVrijednosti(ResourceBundle text) {
        String emailValidatorString = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailValidatorString);
        Matcher m = pattern.matcher(email);
        boolean status = m.matches();
        if (korisnickoIme.isEmpty() || lozinka.isEmpty() || prezime.isEmpty() || email.isEmpty()) {
            prikazOdgovora = text.getString("registracija_nisuSviUpisani");
            return false;
        }
        if (ponovljenaLozinka.isEmpty()) {
            prikazOdgovora = text.getString("registracija_nijeUpisanaPonovljenaLozinka");
            return false;
        }
        if (!lozinka.equals(ponovljenaLozinka)) {
            prikazOdgovora = text.getString("registracija_nisuLozinkaIPonovljenaNisuIste");
            return false;
        }
        if (!status) {
            prikazOdgovora = text.getString("registracija_krivoUpisanaEmailAdresa");
            return false;
        }
        return true;
    }

    public void dodajKorisnika() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle text = ResourceBundle.getBundle("org.foi.nwtis.ljakopov.i18n", context.getViewRoot().getLocale());
        if (provjeraUpisaniVrijednosti(text)) {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("username", korisnickoIme);
            jsonObjectBuilder.add("pass", lozinka);
            jsonObjectBuilder.add("prezime", prezime);
            jsonObjectBuilder.add("email", email);
            UserRest userRest = new UserRest();
            String odgovorServera = userRest.unesiKorisnika(jsonObjectBuilder.build());
            if ("1".equals(odgovorServera)) {
                odgovorServera = text.getString("registracija_krivoUpisanaEmailAdresa");
            } else {
                odgovorServera = text.getString("registracija_neUspjesnaRegistracija");
            }
        }
    }

    private boolean provjeriUpisanePodatke() {
        return !(prijavaKorisnickoIme.isEmpty() || prijavaLozinka.isEmpty());
    }

    public void logiranje() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle text = ResourceBundle.getBundle("org.foi.nwtis.ljakopov.i18n", context.getViewRoot().getLocale());
        if (provjeriUpisanePodatke()) {
            UserRestKorisnickoIme userRestKorisnickoIme = new UserRestKorisnickoIme();
            String json = userRestKorisnickoIme.loginKorisnika(prijavaKorisnickoIme);
            System.out.println(json);
            if (!json.equals("[]")) {
                System.out.println("IZ IFA: " + json);
                JsonReader reader = Json.createReader(new StringReader(json));
                JsonArray array = reader.readArray();
                if (prijavaLozinka.equals(array.getJsonObject(0).getString("pass"))) {
                    System.out.println("USPJEŠNO STE LOGIRANNI");
                    HttpSession session = SesijaKorisnika.dodajSesiju();
                    session.setAttribute("korisnickoIme", prijavaKorisnickoIme);
                    System.out.println("ISPIS SESIJE: " + SesijaKorisnika.dajKorisnickoIme());
                }
            } else {
                greska = text.getString("registracija_PrijavaNijeUspjela");
            }
        } else {
            greska = text.getString("registracija_nisuUpisaniPassIKorisnickoIme");
        }
    }
}
