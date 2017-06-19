/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.zrna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.ljakopov.konfiguracije.Konfiguracija;
import org.foi.nwtis.ljakopov.web.podaci.SesijaKorisnika;
import org.foi.nwtis.ljakopov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author ljakopov
 */
@Named(value = "serverSocketIoTMaster")
@SessionScoped
public class ServerSocketIoTMaster implements Serializable {

    /**
     * Creates a new instance of ServerSocketIoTMaster
     */
    public ServerSocketIoTMaster() {
    }

    private String status;
    private boolean prikaz = false;
    private boolean prikazIotMaster;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPrikaz() {
        return prikaz;
    }

    public void setPrikaz(boolean prikaz) {
        this.prikaz = prikaz;
    }

    public boolean isPrikazIotMaster() {
        return prikazIotMaster;
    }

    public void setPrikazIotMaster(boolean prikazIotMaster) {
        this.prikazIotMaster = prikazIotMaster;
    }

    public void start() {
        prikaz = false;
        String start = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; START;";
        posaljiNaredbu(start);
    }

    public void pause() {
        prikaz = false;
        String pause = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; PAUSE;";
        posaljiNaredbu(pause);
    }

    public void stop() {
        prikaz = false;
        String stop = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; STOP;";
        posaljiNaredbu(stop);
    }

    public void status() {
        prikaz = true;
        String status = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; STATUS;";
        posaljiNaredbu(status);
    }

    /**
     * dio rezervaran za IoT_Master web servis
     */
    public void iotMasterStart() {
        String start = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master START;";
        posaljiNaredbu(start);
    }

    public void iotMasterStop() {
        String stop = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master STOP;";
        posaljiNaredbu(stop);
    }

    public void iotMasterWork() {
        String work = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master WORK;";
        posaljiNaredbu(work);
    }

    public void iotMasterWait() {
        String wait = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master WAIT;";
        posaljiNaredbu(wait);
    }

    public void iotMasterStatus() {
        prikazIotMaster = true;
        String status = "USER " + SesijaKorisnika.dajKorisnickoIme() + "; PASSWD " + SesijaKorisnika.dajKorisnickuLozinku() + "; IoT_Master STATUS;";
        posaljiNaredbu(status);
    }

    public void posaljiNaredbu(String naredba) {
        ServletContext sc = (ServletContext) SlusacAplikacije.getContext();
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

        try {
            socket = new Socket(konf.dajPostavku("server"), Integer.parseInt(konf.dajPostavku("port")));

            is = socket.getInputStream();
            os = socket.getOutputStream();

            os.write(naredba.getBytes());
            os.flush();
            socket.shutdownOutput();

            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }

            System.out.println("Primljeni odgovor: " + sb);
            status = sb.toString();
            //TODO provjeriti ispravnost primljenog odgovora
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
    }

}
