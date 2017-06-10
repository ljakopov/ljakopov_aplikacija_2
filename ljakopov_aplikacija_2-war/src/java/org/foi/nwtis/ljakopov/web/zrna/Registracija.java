/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRest;

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
}
