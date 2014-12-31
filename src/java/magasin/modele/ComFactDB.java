package magasin.modele;
/**
 * classe de mappage poo-relationnel ComFact
 * @author Michel Poriaux
 * @version 1.0
 * @see ComFact
 */

import java.sql.*;
import java.util.*;

public class ComFactDB extends ComFact{

/**
  * connexion à la base de données partagée entre toutes les instances(statique)
  */
    protected static Connection dbConnect=null;
        
  /**
   * constructeur par défaut
   */
    public ComFactDB(){
}
    
    
   /**
   * constructeur paramétré à utiliser  lors de la création de la commande 
   * @see #create()
   * @param fkclient référence du client pour lequel la commande est passée
   */
public ComFactDB(int fkclient)throws Exception{
   super(0,0,null,'c',0,fkclient);
 }    
     
 /**
   * constructeur paramétré à utiliser lors de la récupération en base de données 
     * @param numfact numéro de facture
     * @param datecom date de commande
     * @param etat état de la commande (c,f,p)
     * @param montant montant total de la commande
     * @param fkclient référence du client
     * @param numcommande numéro de commande
     * @see #rechComFact1(int)
     * @see #rechComFact2(int)
   */
public ComFactDB(int numcommande, int numfact, java.util.Date datecom,char etat, float montant, int fkclient){
 super(numcommande,numfact,datecom,etat,montant,fkclient) ;

}
   /**
   * méthode statique permettant de partager la connexion entre toutes les instances de
   * ComFactDB
   * @param nouvdbConnect connexion à la base de données
   */
   public static void setConnection(Connection nouvdbConnect) {
      dbConnect=nouvdbConnect;
   }


/**
   * enregistrement d'une nouvelle commande dans la base de données
   * @throws Exception erreur de création
   */
   public void create() throws Exception{
           CallableStatement   cstmt=null;
       try{
	     String req = "call createcomfact(?,?)";
	     cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
             cstmt.setInt(2,fkclient);
	     cstmt.executeUpdate();
             this.numcommande=cstmt.getInt(1);
             read();//différence par rapport à client car on récupère la Date
	    
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
 * récupération des données d'une comfact sur base de son identifiant
 * @throws Exception code inconnu
 */
   public void read ()throws Exception{
	
	String req = "{?=call readcomfact1(?)}";
        
        CallableStatement cstmt=null;
        try{
        cstmt=dbConnect.prepareCall(req);
        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	cstmt.setInt(2,numcommande);
	cstmt.executeQuery();
        ResultSet rs=(ResultSet)cstmt.getObject(1);
        if(rs.next()){
                this.numfact=rs.getInt("NUMFACT");
	     	this.datecom=rs.getDate("DATECOM");
                this.etat=rs.getString("ETAT").charAt(0);
                this.fkclient=rs.getInt("FKCLIENT");
	     	this.montant=rs.getFloat("MONTANT");
	     	    	    
              }
	      else { 
	             throw new Exception("numéro de commande inconnu");
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
      * mise à jour des données de la comfact sur base de son identifiant
      * @throws Exception erreur de mise à jour
      */

    public void update() throws Exception{
        CallableStatement cstmt=null;

    try{
	String req;     
        if(etat=='c') {
            req = "call updatecomfact1(?)";
        }
        else req="call updatecomfact2(?)";
	cstmt=dbConnect.prepareCall(req);
        PreparedStatement pstm = dbConnect.prepareStatement(req);
	cstmt.setInt(1,numcommande);
	cstmt.executeUpdate();
        read();
            
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
  * effacement d'une comfact sur base de son identifiant
  * @throws Exception erreur d'effacement
  */
    public void delete()throws Exception{
	
           CallableStatement cstmt =null;
	   try{
             ArrayList<ComporteDB> liste=ComporteDB.rechLignes(numcommande);
             for(ComporteDB cmp : liste){
                 cmp.delete();
             }
               
	     String req = "call delcomfact(?)";
	     cstmt = dbConnect.prepareCall(req);
	     cstmt.setInt (1,numcommande);
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
    
    /**
     * méthode permettant de récupérer toutes les informations
     * des lignes d'une commande(infos produit comprises)
     * @return liste de strings
     * @throws Exception aucune ligne de commande
     */
    public ArrayList<String> getInfosCommande()throws Exception{
        ArrayList<String> infosCommande=new ArrayList<String>();
        String req = "{?=call readcomfact3(?)}";
            
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
                int quantite=rs.getInt("QUANTITE");
                float prixachat=rs.getFloat("PRIXACHAT");
                String numprod=rs.getString("NUMPROD");
                String description=rs.getString("DESCRIPTION");
                float totligne=rs.getFloat("TOTLIGNE");
                String ligne=quantite+"-"+prixachat+"-"+numprod+"-"+description+"-"+totligne;  
	     	infosCommande.add(ligne);
	      }
	   
              if(!trouve)throw new Exception("code commande sans ligne de commande");
              else return infosCommande;
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
 * méthode statique permettant de récupérer toutes les commandes concernant un certain client
 * @param fkrech numéro de client recherché
 * @return liste de comfact
 * @throws Exception description inconnue
 */
   public static ArrayList<ComFactDB> rechComFact1(int fkrech)throws Exception{
	    ArrayList<ComFactDB> plusieurs=new ArrayList<ComFactDB>();
	    String req = "{?=call readcomfact4(?)}";
            
	    CallableStatement cstmt=null;
	    try{
	    cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    cstmt.setInt(2,fkrech);
	    cstmt.executeQuery();
            ResultSet rs=(ResultSet)cstmt.getObject(1);
	    boolean trouve=false;
            while(rs.next()){
                trouve=true;
                int numcommande=rs.getInt("NUMCOMMANDE");
	        int numfact=rs.getInt("NUMFACT");
	     	java.util.Date datecom=rs.getDate("DATECOM");
                char etat=rs.getString("ETAT").charAt(0);
                float montant=rs.getFloat("MONTANT");
	     	plusieurs.add(new ComFactDB(numcommande,numfact,datecom,etat,montant,fkrech));
	      }
	   
              if(!trouve)throw new Exception("code client sans commande connue");
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
 * méthode statique permettant de récupérer la commande concernant un certain numéro de facture
 * @param numfactrech numéro de client recherché
 * @return liste de comfact
 * @throws Exception description inconnue
 */
   
   public static ComFactDB rechComFact2(int numfactrech)throws Exception{
	    
	    String req = "{?=call readcomfact2(?)}";
            
	    CallableStatement cstmt=null;
	    try{
	    cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    cstmt.setInt(2,numfactrech);
	    cstmt.executeQuery();
            ResultSet rs=(ResultSet)cstmt.getObject(1);
	    boolean trouve=false;
            if(rs.next()){
                trouve=true;
                int numcommande=rs.getInt("NUMCOMMANDE");
	       	java.util.Date datecom=rs.getDate("DATECOM");
                char etat=rs.getString("ETAT").charAt(0);
                float montant=rs.getFloat("MONTANT");
                int fkclient=rs.getInt("FKCLIENT");
	     	return new ComFactDB(numcommande,numfactrech,datecom,etat,montant,fkclient);
	      }
	     else
              throw new Exception("numéro de facture inconnu");
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
   
} 
