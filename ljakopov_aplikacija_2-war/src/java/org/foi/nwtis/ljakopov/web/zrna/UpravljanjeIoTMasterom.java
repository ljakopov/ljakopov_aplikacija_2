/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.web.kontrole.Izbornik;
import org.foi.nwtis.ljakopov.web.podaci.SesijaKorisnika;
import org.foi.nwtis.ljakopov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Lovro
 */
@Named(value = "upravljanjeIoTMasterom")
@SessionScoped
public class UpravljanjeIoTMasterom implements Serializable {

    private String nazivUredjaja;
    private String idUredjaja;
    private List<Izbornik> listaUredjaji = new ArrayList<Izbornik>();
    private String idUredjaString;
    private String noviId;
    private String noviNaziv;
    private String noviAdresa;

    /**
     * Creates a new instance of UpravljanjeIoTMasterom
     */
    public UpravljanjeIoTMasterom() {
    }

    public String getNazivUredjaja() {
        return nazivUredjaja;
    }

    public void setNazivUredjaja(String nazivUredjaja) {
        this.nazivUredjaja = nazivUredjaja;
    }

    public String getIdUredjaja() {
        return idUredjaja;
    }

    public void setIdUredjaja(String idUredjaja) {
        this.idUredjaja = idUredjaja;
    }

    public List<Izbornik> getListaUredjaji() {
        return listaUredjaji;
    }

    public void setListaUredjaji(List<Izbornik> listaUredjaji) {
        this.listaUredjaji = listaUredjaji;
    }

    public String getIdUredjaString() {
        return idUredjaString;
    }

    public void setIdUredjaString(String idUredjaString) {
        this.idUredjaString = idUredjaString;
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

    public void iotMasterList() {
        listaUredjaji.clear();
        String odgovorServisa = "";
        String list = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master LIST;";
        odgovorServisa = posaljiNaredbu(list);
        //System.out.println("OVO JE PREUZETI odgovor: " + odgovorServisa);
        String[] uredjaji = odgovorServisa.split("; ");
        for (String uredjaji1 : uredjaji) {
            int id = uredjaji1.indexOf(" ");
            int idEnd = uredjaji1.indexOf(" A");
            int adresaEnd = uredjaji1.lastIndexOf(": ");
            idUredjaja = uredjaji1.substring(id, idEnd).replace(" ", "");
            nazivUredjaja = uredjaji1.substring(adresaEnd).replace(": ", "");
            listaUredjaji.add(new Izbornik(idUredjaja, nazivUredjaja));
        }
    }

    public void iotMasterLoad() {
        String odgovorServisa = "";
        String load = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master LOAD;";
        odgovorServisa = posaljiNaredbu(load);

    }

    public void iotMasterClear() {
        String odgovorServisa = "";
        String clear = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master CLEAR;";
        odgovorServisa = posaljiNaredbu(clear);
    }

    public void iotMasterWorkUredjaj() {
        //System.out.println("OVO JE ID: " + idUredjaString);
        String odgovorServisa = "";
        String work = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT " + this.idUredjaString + " WORK;";
        odgovorServisa = posaljiNaredbu(work);
    }

    public void iotMasterWaitUredjaj() {
        //System.out.println("OVO JE ID: " + idUredjaString);
        String odgovorServisa = "";
        String wait = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT " + this.idUredjaString + " WAIT;";
        odgovorServisa = posaljiNaredbu(wait);
    }

    public void iotMasterRemoveUredjaj() {
        //System.out.println("OVO JE ID: " + idUredjaString);
        String odgovorServisa = "";
        String remove = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT " + this.idUredjaString + " REMOVE;";
        odgovorServisa = posaljiNaredbu(remove);
    }

    public void iotMasterStatusUredjaj() {
        //System.out.println("OVO JE ID: " + idUredjaString);
        String odgovorServisa = "";
        String status = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT " + this.idUredjaString + " STATUS;";
        odgovorServisa = posaljiNaredbu(status);
    }

    public void iotMasterAddUredjaj() {
        //System.out.println("OVO JE : " + noviId + " " + noviNaziv + " " + noviAdresa);
        String odgovorServisa = "";
        String add = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT " + this.noviId + " ADD " + '"' + this.noviNaziv
               + '"' + " " + '"' + this.noviAdresa + '"' + ";";
        System.out.println("ISPIS ADD: " + add);
        odgovorServisa = posaljiNaredbu(add);

    }

    public String posaljiNaredbu(String naredba) {
        ServletContext sc = (ServletContext) SlusacAplikacije.getContext();
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

        String odgovor = "";
        try {
            socket = new Socket(konf.dajPostavku("server"), Integer.parseInt(konf.dajPostavku("port")));

            is = socket.getInputStream();
            os = socket.getOutputStream();

            os.write(naredba.getBytes(Charset.forName("UTF-8")));
            os.flush();
            socket.shutdownOutput();

            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder sb = new StringBuilder();
            Reader in = new InputStreamReader(is, "UTF-8");
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0) {
                    break;
                }
                sb.append(buffer, 0, rsz);
            }
            in.close();
            System.out.println("Primljeni odgovor: " + sb);
            odgovor = sb.toString();

        } catch (IOException ex) {
            Logger.getLogger(ServerSocketIoTMaster.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerSocketIoTMaster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return odgovor;
    }
}
