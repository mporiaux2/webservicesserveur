/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magasin.modele;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class ListeProduits {
    
    //@XmlElement(name = "produit")
    private ArrayList<ProduitDB> liste= new ArrayList<ProduitDB>();
    
    public ListeProduits(){
        
    }

    public ArrayList<ProduitDB> getListe() {
        return liste;
    }

    public void setListe(ArrayList<ProduitDB> liste) {
        this.liste = liste;
    }
    
    
    
}
