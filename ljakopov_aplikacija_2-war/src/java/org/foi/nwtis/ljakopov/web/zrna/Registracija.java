/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
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
    
    private boolean provjeraUpisaniVrijednosti(){
        if(korisnickoIme.isEmpty() || lozinka.isEmpty() || prezime.isEmpty() || email.isEmpty()){
            return false;
        }
        else if(ponovljenaLozinka.isEmpty()){
            return false;
        }
        else if(lozinka.equals(ponovljenaLozinka)){
            return false;
        }
        
        return false;
    }
    
    public void dodajKorisnika() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("username", korisnickoIme);
        jsonObjectBuilder.add("pass", lozinka);
        jsonObjectBuilder.add("prezime", prezime);
        jsonObjectBuilder.add("email", email);
        //System.out.println("ISPIS: " + jsonObjectBuilder.build().toString());
        //meteoRESTKlijentPost.zapisiRest(jsonObjectBuilder.build());
        UserRest userRest = new UserRest();
        String a = userRest.unesiKorisnika(jsonObjectBuilder.build());
        System.out.println("OVO JE AAAA: " + a);
    }
}
