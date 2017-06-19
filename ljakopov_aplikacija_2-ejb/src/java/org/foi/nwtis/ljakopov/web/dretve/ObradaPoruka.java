/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.ljakopov.web.dretve;

import java.io.IOException;
import static java.lang.Thread.MIN_PRIORITY;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.foi.nwtis.ljakopov.zrna.SingSessionBean;

/**
 *
 * @author Lovro
 */
public class ObradaPoruka extends Thread {

    private boolean prekid_obrade = false;

    @Override
    public void interrupt() {
        System.out.println("OVO JE PREKID");
        prekid_obrade = true;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {

        try {
            sleep(6000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.korisnickoIme);
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.lozinka);
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.server);
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.intervalDretve);
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.nwtisPoruke);
        System.out.println("ISPIS IZ DRETV: " + SingSessionBean.korisnickoIme);

        while (!prekid_obrade) {
            try {
                long pocetak = System.currentTimeMillis();
                java.util.Properties properties = System.getProperties();
                properties.put("mail.smtp.host", SingSessionBean.server);
                Session session = Session.getInstance(properties, null);

                Store store = session.getStore("imap");
                store.connect(SingSessionBean.server, SingSessionBean.korisnickoIme, SingSessionBean.lozinka);

                Folder folderZaSpremanje = store.getDefaultFolder();
                if (folderZaSpremanje.getFolder(SingSessionBean.nwtisPoruke).exists() != true) {
                    folderZaSpremanje.getFolder(SingSessionBean.nwtisPoruke).create(Folder.HOLDS_MESSAGES);
                }

                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);

                Folder folderZaSpremanjeIspravnihPoruka = store.getFolder(SingSessionBean.nwtisPoruke);
                folderZaSpremanjeIspravnihPoruka.open(Folder.READ_WRITE);

                Message[] messages = folder.getMessages();
                Message[] porukaZaPremjestanje = new Message[MIN_PRIORITY];

                for (int i = 0; i < messages.length; ++i) {
                    porukaZaPremjestanje[0] = messages[i];
                    System.out.println("OVO SU PORUKE: " + messages[i].getSubject());

                    if (messages[i].getSubject().equals(SingSessionBean.nwtisPoruke) && (messages[i].getContent() instanceof String)) {
                        System.out.println("OVO SU PORUKE i NWTIS PORUKA: " + messages[i].getSubject());
                        folder.copyMessages(porukaZaPremjestanje, folderZaSpremanjeIspravnihPoruka);
                        messages[i].setFlag(Flag.DELETED, true);
                    }
                }
                System.out.println("GOTOVO");
                folderZaSpremanjeIspravnihPoruka.close(true);
                folder.close(true);
                store.close();
                long kraj = System.currentTimeMillis();
                sleep((SingSessionBean.intervalDretve * 1000) - (kraj - pocetak));

            } catch (NoSuchProviderException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException | IOException | InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
