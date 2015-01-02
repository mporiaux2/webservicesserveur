package magasin.modele;

import java.sql.*;
import java.util.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * classe de mappage poo-relationnel comporte
 * @author Michel Poriaux
 * @version 1.0
 * @see Comporte
 */

@XmlRootElement(name="comporte")
public class ComporteDB extends Comporte{

  /**
  * connexion à la base de données partagée entre toutes les instances(statique)
  */
    protected static Connection dbConnect=null;
        
  /**
   * constructeur par défaut
   */
  public ComporteDB(){
  }
  
  
  /**
   * constructeur paramétré utilisé lors de la création
   * @see #create()
   * @param fkcomfact référence commande
   * @param fkproduit référence produit
   * @param quantite  quantité achetée
   */
  public ComporteDB(int fkcomfact,String fkproduit,int quantite){
     super(0,fkcomfact,fkproduit,quantite,0);
  }
   /**
    * constructeur paramétré utilisé lors de la lecture en base de données
    * @param idcomporte identifiant de la ligne de commande
    * @param fkcomfact référence commande
    * @param fkproduit référence produit
    * @param quantite  quantité achetée
    * @param prixachat prix du produit lors de l'achat
    */
  public ComporteDB(int idcomporte,int fkcomfact,String fkproduit,int quantite,float prixachat){
     super(idcomporte,fkcomfact,fkproduit,quantite,prixachat);
  }
  /**
   * méthode statique permettant de partager la connexion entre toutes les instances de
   * ComporteDB
   * @param nouvdbConnect connexion à la base de données
   */
   public static void setConnection(Connection nouvdbConnect) {
      dbConnect=nouvdbConnect;
   }

  /**
   * enregistrement d'une nouvelle ligne de commande dans la base de données
   * @throws Exception erreur de création
   */
   public void create() throws Exception{
        CallableStatement   cstmt=null;
       try{
	     String req = "call createcomporte(?,?,?,?,?)";
	     cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
             cstmt.setInt(2,fkcomfact);
	     cstmt.setString(3,fkproduit);
	     cstmt.setInt(4,quantite);
             cstmt.registerOutParameter(5, java.sql.Types.FLOAT);
	     cstmt.executeUpdate();
             this.idligne=cstmt.getInt(1);
	     this.prixachat=cstmt.getFloat(5);
             
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
 * récupération des données d'une ligne de commande sur base de son identifiant
 * @throws Exception identifiant inconnu
 */
   public void read ()throws Exception{
	
	String req = "{?=call readcomporte1(?)}";
        
        CallableStatement cstmt=null;
        try{
        cstmt=dbConnect.prepareCall(req);
        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	cstmt.setInt(2,idligne);
	cstmt.executeQuery();
        ResultSet rs=(ResultSet)cstmt.getObject(1);
        if(rs.next()){
	     	this.fkcomfact=rs.getInt(2);
	     	this.fkproduit=rs.getString(3);
	     	this.quantite=rs.getInt(4);
	     	this.prixachat=rs.getFloat(5);
	     		        
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
 * méthode statique de récupération des lignes de commande correspondant
 * à un numéro de commande donné
 * @param numcommande numéro de commande recherché
 * @return liste de lignes de commandes
 * @throws Exception commande sans ligne de commande
 */
   public static ArrayList<ComporteDB> rechLignes(int numcommande)throws Exception{
	    ArrayList<ComporteDB> plusieurs=new ArrayList<ComporteDB>();
	    String req = "{?=call readcomporte2(?)}";
            
	    CallableStatement cstmt=null;
	    try{
	    cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    cstmt.setInt(2,numcommande);
	    cstmt.executeQuery();
            ResultSet rs=(ResultSet)cstmt.getObject(1);
	    boolean trouve=false;
            while(rs.next()){
                trouve=true;
	        int idcomporte=rs.getInt(1);
                int fkcomfact=rs.getInt(2);//superflu car identique à numcommande
	     	String fkproduit=rs.getString(3);
                int quantite=rs.getInt(4);
                float pa=rs.getFloat(5);
	     	plusieurs.add(new ComporteDB(idcomporte,fkcomfact,fkproduit,quantite,pa));
	      }
	    return plusieurs;
            //ligne suivante désactivée car génère exceptions quand commande vide
            /*
              if(!trouve)throw new Exception("numéro de commande inconnu");
              else return plusieurs;*/
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
      * mise à jour des données 
      * @throws Exception erreur de mise à jour
      */

    public void update() throws Exception{
        CallableStatement cstmt=null;

    try{
	     String req = "call updatecomporte(?,?)";
	     cstmt=dbConnect.prepareCall(req);
             PreparedStatement pstm = dbConnect.prepareStatement(req);
	     cstmt.setInt(1,idligne);
	     cstmt.setInt(2,quantite);
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
  * effacement d'une ligne de commande sur base de son identifiant
  * @throws Exception erreur d'effacement
  */
 
    public void delete()throws Exception{
	
           CallableStatement cstmt =null;
	   try{
	     String req = "call delcomporte(?)";
	     cstmt = dbConnect.prepareCall(req);
	     cstmt.setInt(1,idligne);
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
