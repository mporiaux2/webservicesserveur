/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magasin.modele;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class ListeComporte {
    
    //@XmlElement(name = "produit")
    private ArrayList<ComporteDB> liste= new ArrayList<ComporteDB>();
    
    public ListeComporte(){
        
    }

    public ArrayList<ComporteDB> getListe() {
        return liste;
    }

    public void setListe(ArrayList<ComporteDB> liste) {
        this.liste = liste;
    }
    
    
    
}
