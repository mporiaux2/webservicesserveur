
package service;

import java.sql.Connection;
import javax.ws.rs.Path;
import magasin.modele.ClientDB;
import magasin.modele.ComFactDB;
import magasin.modele.ComporteDB;
import magasin.modele.ProduitDB;
import myconnections.DBConnection;

@Path("/gestion")
public class Gestion {
    static{
        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.out.println("connection invalide");
        }
        else {
            System.out.println("connexion établie");
            ProduitDB.setConnection(dbConnect);
            ClientDB.setConnection(dbConnect);
            ComFactDB.setConnection(dbConnect);
            ComporteDB.setConnection(dbConnect);
        }
    }
    
    @Path("produits")
    public GestionProduits gestionProduits(){
       return new GestionProduits(); 
    }
    
     @Path("clients")
    public GestionClients gestionClients(){
       return new GestionClients(); 
    }
    
      @Path("commandes")
    public GestionCommandes gestionCommandes(){
       return new GestionCommandes(); 
    }
}
