/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.podaci;

/**
 *
 * @author ljakopov
 */
public class KorisnikPodaci {

    public int ID;
    public String korisnickoIme;
    //public String lozinka;
    public String prezime;
    public String email;

    public KorisnikPodaci(int ID, String korisnickoIme, String prezime, String email) {
        this.ID = ID;
        this.korisnickoIme = korisnickoIme;
        this.prezime = prezime;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

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

}
