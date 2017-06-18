/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRest;
import org.foi.nwtis.ljakopov.rest.klijenti.UserRestKorisnickoIme;
import org.foi.nwtis.ljakopov.web.podaci.KorisnikPodaci;
import org.foi.nwtis.ljakopov.web.podaci.SesijaKorisnika;

/**
 *
 * @author ljakopov
 */
@Named(value = "korisnik")
@SessionScoped
public class Korisnik implements Serializable {

    private List<KorisnikPodaci> podaciSvihKorisnika = new ArrayList<>();
    private String lozinka;
    private String id;
    private String email;
    private String prezime;
    private String ponovljenaLozinka;
    int korisniciPoStranici = 7;
    private String dobivenaLozinka;
    private String greske;
    private String korisnickoIme;

    /**
     * Creates a new instance of Korisnik
     */
    public Korisnik() {
        preuzmiSveKorisnike();
        dohvatiSvePodatkeKorisnika();
    }

    public List<KorisnikPodaci> getPodaciSvihKorisnika() {
        return podaciSvihKorisnika;
    }

    public void setPodaciSvihKorisnika(List<KorisnikPodaci> podaciSvihKorisnika) {
        this.podaciSvihKorisnika = podaciSvihKorisnika;
    }

    public int getKorisniciPoStranici() {
        return korisniciPoStranici;
    }

    public void setKorisniciPoStranici(int korisniciPoStranici) {
        this.korisniciPoStranici = korisniciPoStranici;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPonovljenaLozinka() {
        return ponovljenaLozinka;
    }

    public void setPonovljenaLozinka(String ponovljenaLozinka) {
        this.ponovljenaLozinka = ponovljenaLozinka;
    }

    public String getGreske() {
        return greske;
    }

    public void setGreske(String greske) {
        this.greske = greske;
    }
    
    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public void preuzmiSveKorisnike() {
        //System.out.println("SESIJA JE: " + SesijaKorisnika.dajKorisnickoIme());
        podaciSvihKorisnika.clear();
        UserRest userRest = new UserRest();
        String jsonSviKorisnici = userRest.dajSveKorisnike();
        JsonReader reader = Json.createReader(new StringReader(jsonSviKorisnici));
        JsonArray array = reader.readArray();
        for (int i = 0; i < array.size(); i++) {
            podaciSvihKorisnika.add(new KorisnikPodaci(array.getJsonObject(i).getInt("id"), array.getJsonObject(i).getString("username"), array.getJsonObject(i).getString("prezime"), array.getJsonObject(i).getString("email")));
        }
    }

    public void dohvatiSvePodatkeKorisnika() {
        UserRestKorisnickoIme userRestKorisnickoIme = new UserRestKorisnickoIme();
        String json = userRestKorisnickoIme.loginKorisnika(SesijaKorisnika.dajKorisnickoIme());
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray array = reader.readArray();
        System.out.println(json);
        id = Integer.toString(array.getJsonObject(0).getInt("id"));
        korisnickoIme = array.getJsonObject(0).getString("username");
        dobivenaLozinka = array.getJsonObject(0).getString("pass");
        email = array.getJsonObject(0).getString("email");
        prezime = array.getJsonObject(0).getString("prezime");
        System.out.println("ISPIS: " + id + " " + lozinka + " " + email);
    }

    private boolean provjeraUpisanihPodataka() {
        return !(email.isEmpty() || prezime.isEmpty() || korisnickoIme.isEmpty());
    }

    private boolean provjeraLozinkeIPonovljeneLozinke() {
        return !(lozinka.isEmpty() && ponovljenaLozinka.isEmpty());
    }

    //TODO dodati prikaz greški
    public void azurirajPodatkeKorisnika() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle text = ResourceBundle.getBundle("org.foi.nwtis.ljakopov.i18n", context.getViewRoot().getLocale());
        if (provjeraUpisanihPodataka()) {
            System.out.println("ISPIS: " + id + " " + lozinka + " " + email + " " + korisnickoIme);
            if (provjeraLozinkeIPonovljeneLozinke()) {
                System.out.println("LOZINKE");
                if (lozinka.equals(ponovljenaLozinka)) {
                    System.out.println("LOZIKE SU ISTE");
                    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                    jsonObjectBuilder.add("username", korisnickoIme);
                    jsonObjectBuilder.add("pass", lozinka);
                    jsonObjectBuilder.add("prezime", prezime);
                    jsonObjectBuilder.add("email", email);
                    String dobiveniOdgovor = UserRestKorisnickoIme.azurirajKorisnikaJson(SesijaKorisnika.dajKorisnickoIme(), jsonObjectBuilder.build());
                    if ("1".equals(dobiveniOdgovor)) {
                        greske = text.getString("pregledKorisnika_azuriranjeUspjesno");
                        preuzmiSveKorisnike();
                        HttpSession session = SesijaKorisnika.dodajSesiju();
                        session.setAttribute("korisnickoIme", korisnickoIme);
                        session.setAttribute("lozinka", lozinka);
                    } else {
                        greske = text.getString("pregledKorisnika_azuriranjeNeuspjesno");
                    }
                } else {
                    greske = text.getString("pregledKorisnika_nisuLozinkaIPonovljenaNisuIste");
                }
            } else {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("username", korisnickoIme);
                jsonObjectBuilder.add("pass", SesijaKorisnika.dajKorisnickuLozinku());
                jsonObjectBuilder.add("prezime", prezime);
                jsonObjectBuilder.add("email", email);
                String dobiveniOdgovor = UserRestKorisnickoIme.azurirajKorisnikaJson(SesijaKorisnika.dajKorisnickoIme(), jsonObjectBuilder.build());
                if ("1".equals(dobiveniOdgovor)) {
                    greske = text.getString("pregledKorisnika_azuriranjeUspjesno");
                    preuzmiSveKorisnike();
                    HttpSession session = SesijaKorisnika.dodajSesiju();
                    session.setAttribute("korisnickoIme", korisnickoIme);
                    //session.setAttribute("lozinka", lozinka);
                } else {
                    greske = text.getString("pregledKorisnika_azuriranjeNeuspjesno");
                }
            }
        } else {
            greske = text.getString("pregledKorisnika_emailIPrezimeNijeUpisano");
        }
    }



}
