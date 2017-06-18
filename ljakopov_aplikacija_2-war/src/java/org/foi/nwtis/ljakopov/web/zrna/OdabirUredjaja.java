/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.foi.nwtis.ljakopov.rest.klijenti.MeteoRest;
import org.foi.nwtis.ljakopov.rest.klijenti.MeteoRestId;
import org.foi.nwtis.ljakopov.web.podaci.Lokacija;
import org.foi.nwtis.ljakopov.web.podaci.SesijaKorisnika;
import org.foi.nwtis.ljakopov.web.podaci.Uredjaj;
import org.foi.nwtis.ljakopov.ws.klijenti.MeteoWsKlijent;
import org.foi.nwtis.ljakopov.ws.serveri.MeteoPodaci;

/**
 *
 * @author ljakopov
 */
@Named(value = "odabirUredjaja")
@SessionScoped
public class OdabirUredjaja implements Serializable {

    /**
     * Creates a new instance of OdabirUredjaja
     */
    public OdabirUredjaja() {
        preuzmiSveUredjaje();
    }

    private MeteoPodaci meteoPodaci = new MeteoPodaci();
    private List<Uredjaj> uredjaji = new ArrayList<>();
    private String id;
    private int idUredjaja;
    private String naziv;
    private String adresa;
    private boolean azuriranje = false;
    private String greska;
    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    private String adresaUredjaja;
    private boolean prikazIspisAdrese = false;

    public List<Uredjaj> getUredjaji() {
        return uredjaji;
    }

    public void setUredjaji(List<Uredjaj> uredjaji) {
        this.uredjaji = uredjaji;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getIdUredjaja() {
        return idUredjaja;
    }

    public void setIdUredjaja(int idUredjaja) {
        this.idUredjaja = idUredjaja;
    }

    public boolean isAzuriranje() {
        return azuriranje;
    }

    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    public String getGreska() {
        return greska;
    }

    public void setGreska(String greska) {
        this.greska = greska;
    }

    public String getNoviId() {
        return noviId;
    }

    public void setNoviId(String noviId) {
        this.noviId = noviId;
    }

    public String getNoviNaziv() {
        return noviNaziv;
    }

    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    public String getNoviAdresa() {
        return noviAdresa;
    }

    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    public String getAdresaUredjaja() {
        return adresaUredjaja;
    }

    public void setAdresaUredjaja(String adresaUredjaja) {
        this.adresaUredjaja = adresaUredjaja;
    }

    public boolean isPrikazIspisAdrese() {
        return prikazIspisAdrese;
    }

    public void setPrikazIspisAdrese(boolean prikazIspisAdrese) {
        this.prikazIspisAdrese = prikazIspisAdrese;
    }

    public void preuzmiSveUredjaje() {
        uredjaji.clear();
        String jsonSviUredjaji = MeteoRest.preuzmiSveUredjaje();
        JsonReader reader = Json.createReader(new StringReader(jsonSviUredjaji));
        JsonArray array = reader.readArray();
        for (int i = 0; i < array.size(); i++) {
            Lokacija lokacija = new Lokacija(array.getJsonObject(i).getString("lat"), array.getJsonObject(i).getString("lon"));
            uredjaji.add(new Uredjaj(array.getJsonObject(i).getInt("uid"), array.getJsonObject(i).getString("naziv"), lokacija));
        }
    }

    public void pogledajUredjaj() {
        if (!id.isEmpty()) {
            azuriranje = true;
            //greska = "krivo";
            System.out.println("OVO JE ID: " + this.id);
            String jsonUredjaj = MeteoRestId.dohvatiUredjaj(this.id);
            JsonReader reader = Json.createReader(new StringReader(jsonUredjaj));
            System.out.println("OVO JE JSON : " + jsonUredjaj);
            JsonArray array = reader.readArray();
            idUredjaja = array.getJsonObject(0).getInt("uid");
            naziv = array.getJsonObject(0).getString("naziv");
            System.out.println("OVO JE NAZIV: " + naziv);
            adresa = array.getJsonObject(0).getString("adresa");
        }
    }

    public void azurirajUredjaj() {
        System.out.println("ISPIS: " + naziv + " " + adresa);
        if (!naziv.isEmpty() && !adresa.isEmpty()) {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("naziv", naziv);
            jsonObjectBuilder.add("adresa", adresa);
            String odgovor = MeteoRestId.azurirajIoTUredjaj(id, jsonObjectBuilder.build());
            System.out.println("OS: " + odgovor);
            azuriranje = false;
            preuzmiSveUredjaje();
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public void dodajIoTUredaj() {
        String odgovor;
        if (!noviId.isEmpty() && !noviAdresa.isEmpty() && !noviNaziv.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle text = ResourceBundle.getBundle("org.foi.nwtis.ljakopov.i18n", context.getViewRoot().getLocale());
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("id", noviId);
            jsonObjectBuilder.add("naziv", noviNaziv);
            jsonObjectBuilder.add("adresa", noviAdresa);
            odgovor = MeteoRest.dodajUredjaj(jsonObjectBuilder.build());
            if (odgovor.equals("1")) {
                preuzmiSveUredjaje();
                greska = text.getString("uredjaji_uspjesnoDodavanje");
            } else {
                greska = text.getString("uredjaji_neUsjesnoDodavanje");
            }
        }
    }

    public void dajAdresu() {
        if (id != null) {
            prikazIspisAdrese = true;
            String lat;
            String lon;
            String jsonUredjaj = MeteoRestId.dohvatiUredjaj(this.id);
            JsonReader reader = Json.createReader(new StringReader(jsonUredjaj));
            System.out.println("OVO JE JSON : " + jsonUredjaj);
            JsonArray array = reader.readArray();

            lat = array.getJsonObject(0).getString("lat");
            lon = array.getJsonObject(0).getString("lon");
            adresaUredjaja = MeteoWsKlijent.dajAdresuNaTemeljuLatLon(SesijaKorisnika.dajKorisnickoIme(), SesijaKorisnika.dajKorisnickuLozinku(), lat, lon);
            System.out.println("OVO JE ADRESA; " + adresaUredjaja);
        }
    }

    public void dajVazecePodatke() {
        if (id != null) {
            meteoPodaci = MeteoWsKlijent.dajMeteoPodatkeZaUredjaj(SesijaKorisnika.dajKorisnickoIme(), SesijaKorisnika.dajKorisnickuLozinku(), Integer.parseInt(this.id));
        }
    }

    public void dajZadnjiPodatakZaUredjaj() {
        if (id != null) {
            meteoPodaci = MeteoWsKlijent.dajZadnjiMeteoPodatakZaUredjaj(SesijaKorisnika.dajKorisnickoIme(), SesijaKorisnika.dajKorisnickuLozinku(), Integer.parseInt(this.id));
        }
    }

}
