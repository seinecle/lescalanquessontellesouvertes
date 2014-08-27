/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.lescalanquessontellesouvertes;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author C. Levallois
 */
@ManagedBean
@RequestScoped
public class Controller {

    private String text;
    private boolean exceptions;
    private String communes;
    private boolean calanquesFound;
    private boolean orangeFound;
    private boolean redFound;
    private String areCalanquesOpen;
    private boolean error;

    public void init() {
        try {
            error = false;
            Document doc = Jsoup.connect("http://cg13.eway.fr/conditions.php").get();
            Elements lineCalanques = doc.select("tr");
            Iterator<Element> it = lineCalanques.iterator();

            while (it.hasNext()) {
                if (calanquesFound) {
                    break;
                }
                Element tr = it.next();
                calanquesFound = false;
                orangeFound = false;
                redFound = false;
                int counter = 0;
                int numLineCalanques = 999999;

                Iterator<Element> it2 = tr.children().iterator();
                while (it2.hasNext()) {
                    counter++;
                    Element tdOriginal = it2.next();
                    String td = tdOriginal.text().toLowerCase();
                    if (td.equals("les calanques entre marseille et cassis")) {
                        calanquesFound = true;
                        numLineCalanques = counter;
                    }
                    if (counter == numLineCalanques + 1) {
                        if (td.equals("orange")) {
                            orangeFound = true;
                        }
                        if (td.equals("rouge")) {
                            redFound = true;
                        }
                    }
                    if (counter == numLineCalanques + 2) {
                        if (!td.isEmpty()) {
                            exceptions = true;
                            communes = tdOriginal.text();
                        }
                    }
                }

            }
        } catch (IOException ex) {
            error = true;
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getAreCalanquesOpen() {
        if (error) {
            return "je ne sais pas";
        } else {
            return (orangeFound || redFound) ? "oui" : "non";
        }
    }

    public void setAreCalanquesOpen(String areCalanquesOpen) {
        this.areCalanquesOpen = areCalanquesOpen;
    }

    public boolean isRedFound() {
        return redFound;
    }

    public void setRedFound(boolean redFound) {
        this.redFound = redFound;
    }

    public boolean isExceptions() {
        return exceptions;
    }

    public void setExceptions(boolean exceptions) {
        this.exceptions = exceptions;
    }

    public String getCommunes() {
        return communes;
    }

    public void setCommunes(String communes) {
        this.communes = communes;
    }
    
    

}
