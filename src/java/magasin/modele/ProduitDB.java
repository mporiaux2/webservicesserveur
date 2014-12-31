package magasin.modele;
import java.util.*;
import java.sql.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * classe de mappage poo-relationnel Produit
 * @author Michel Poriaux
 * @version 1.0
 * @see Produit
 */

@XmlRootElement(name="produit")
public class ProduitDB extends Produit {
/**
  * connexion à la base de données partagée entre toutes les instances(statique)
  */
    protected static Connection dbConnect=null;
        
  /**
   * constructeur par défaut
   */
    public ProduitDB(){
}
    
    
   /**
   * constructeur paramétré à utiliser lors de la création ou de la restauration du produit
   * @see #create()
   * @param numprod numéro de produit
   * @param description description du produit
   * @param phtva prix hors tva du produit
   * @param stock stock en cours du produit
  */
public ProduitDB(String numprod,String description,float phtva,int stock){
super(numprod,description,phtva,stock);
}



  /**
   * constructeur à utiliser lorsque le produit est déjà présent en base de données
   * mais qu'on ne connaît que son identifiant, à utiliser avec read
   * @see #read()
   * @param numprod
   */
public ProduitDB(String numprod){
    super(numprod,"",0,0);
}



  /**
   * méthode statique permettant de partager la connexion entre toutes les instances de
   * ProduitDB
   * @param nouvdbConnect connexion à la base de données
   */
   public static void setConnection(Connection nouvdbConnect) {
      dbConnect=nouvdbConnect;
   }


/**
   * enregistrement d'un nouveau produit dans la base de données
   * @throws Exception erreur de création
   */
   public void create() throws Exception{
        CallableStatement   cstmt=null;
       try{
	     String req = "call createproduit(?,?,?,?)";
	     cstmt = dbConnect.prepareCall(req);
             cstmt.setString(1,numprod);
	     cstmt.setString(2,description);
	     cstmt.setFloat(3,phtva);
	     cstmt.setInt(4,stock);
	     cstmt.executeUpdate();
                 
       }
       catch(Exception e ){
          
                throw new Exception("Erreur de création "+e.getMessage());
             }
       finally{//effectué dans tous les cas 
            try{
              cstmt.close();
            }
            catch (Exception e){}
        }
       }
/**
 * récupération des données d'un produit sur base de son identifiant
 * @throws Exception code inconnu
 */
   public void read ()throws Exception{
	
	String req = "{?=call readproduit1(?)}";
        
        CallableStatement cstmt=null;
        try{
        cstmt=dbConnect.prepareCall(req);
        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	cstmt.setString(2,numprod);
	cstmt.executeQuery();
        ResultSet rs=(ResultSet)cstmt.getObject(1);
        if(rs.next()){
	     	this.description=rs.getString("DESCRIPTION");
	     	this.phtva=rs.getFloat("phtva");
	     	this.stock=rs.getInt("STOCK");
	     	    
              }
	      else { 
	             throw new Exception("Code inconnu");
	      }

            }
	catch(Exception e){
             
                throw new Exception("Erreur de lecture "+e.getMessage());
             }
        finally{//effectué dans tous les cas 
            try{
              cstmt.close();
            }
            catch (Exception e){}
        }
     }
/**
 * méthode statique permettant de récupérer tous les produits comportant une certaine description
 * @param descrrech description recherchée
 * @return liste de produits
 * @throws Exception description inconnue
 */
   public static ArrayList<ProduitDB> rechProduit(String descrrech)throws Exception{
	    ArrayList<ProduitDB> plusieurs=new ArrayList<ProduitDB>();
	    String req = "{?=call readproduit2(?)}";
            descrrech=descrrech.trim();//pour accepter les requetes webservice avec un blanc
	    CallableStatement cstmt=null;
	    try{
	    cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    cstmt.setString(2,descrrech);
	    cstmt.executeQuery();
            ResultSet rs=(ResultSet)cstmt.getObject(1);
	    boolean trouve=false;
            while(rs.next()){
                trouve=true;
	        String numprod=rs.getString("NUMPROD");
	     	String description=rs.getString("DESCRIPTION");
	     	float phtva=rs.getFloat("PHTVA");
	     	int stock=rs.getInt("STOCK");
	     	plusieurs.add(new ProduitDB(numprod,description,phtva,stock));
	      }
	   
              if(!trouve)throw new Exception("description inconnue");
              else return plusieurs;
	     }
	     catch(Exception e){
		
                throw new Exception("Erreur de lecture "+e.getMessage());
             }
            finally{//effectué dans tous les cas 
            try{
              cstmt.close();
            }
            catch (Exception e){}
        }
     }   
   
  /**
      * mise à jour des données du produit sur base de son identifiant
      * @throws Exception erreur de mise à jour
      */

    public void update() throws Exception{
        CallableStatement cstmt=null;

    try{
	     String req = "call updateproduit(?,?,?,?)";
	     cstmt=dbConnect.prepareCall(req);
             PreparedStatement pstm = dbConnect.prepareStatement(req);
	     cstmt.setString(1,numprod);
	     cstmt.setString(2,description);
	     cstmt.setFloat(3,phtva);
	     cstmt.setInt(4,stock);
	     cstmt.executeUpdate();
            
    }

	  catch(Exception e){
	  	
                throw new Exception("Erreur de mise à jour "+e.getMessage());
             }
          finally{//effectué dans tous les cas 
            try{
              cstmt.close();
            }
            catch (Exception e){}
        }
    }
 /**
  * effacement d'un produit sur base de son identifiant
  * @throws Exception erreur d'effacement
  */
    public void delete()throws Exception{
	
           CallableStatement cstmt =null;
	   try{
	     String req = "call delproduit(?)";
	     cstmt = dbConnect.prepareCall(req);
	     cstmt.setString (1,numprod);
	     cstmt.executeUpdate();
	     	     
	     }	
	   catch (Exception e){
	     	
                throw new Exception("Erreur d'effacement "+e.getMessage());
             }
           finally{//effectué dans tous les cas 
            try{
              cstmt.close();
            }
            catch (Exception e){}
          }
   	} 
   
}