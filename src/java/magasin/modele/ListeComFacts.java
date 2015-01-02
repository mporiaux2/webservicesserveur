/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magasin.modele;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class ListeComFacts {
    
    
    private ArrayList<ComFactDB> liste= new ArrayList<ComFactDB>();
    
    public ListeComFacts(){
        
    }

    public ArrayList<ComFactDB> getListe() {
        return liste;
    }

    public void setListe(ArrayList<ComFactDB> liste) {
        this.liste = liste;
    }
    
    
    
}
