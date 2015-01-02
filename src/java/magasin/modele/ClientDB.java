package magasin.modele ;
/**
 * classe de mappage poo-relationnel client
 * @author Michel Poriaux
 * @version 1.0
 * @see Client
 */
import java.sql.*;
import java.util.*;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="client")
public class ClientDB extends Client implements CRUD {
 /**
  * connexion à la base de données partagée entre toutes les instances(statique)
  */
    protected static Connection dbConnect=null;
        
  /**
   * constructeur par défaut
   */
  public ClientDB(){
   	}

  /**
   * constructeur paramétré à utiliser avant lors de la création, l'idclient
   * ne doit pas être précisé,il sera affecté par la base de données lors de la création
   * @see #create()
   * @param nom nom du client
   * @param prenom prénom du client
   * @param cp code postal de l'adresse
   * @param localite localité de l'adresse
   * @param rue rue de l'adresse
   * @param num numéro de l'adresse
   * @param tel téléphone du client
   */
   public ClientDB(String nom,String prenom,int cp,
    String localite,String rue,String num,String tel){	
      super(0,nom,prenom,cp,localite,rue,num,tel);
   }

 /**
   * constructeur paramétré à utiliser avant lorsque le client est déjà présent
   * dans la base de données
   * @param idclient identifiant du client
   * @param nom nom du client
   * @param prenom prénom du client
   * @param cp code postal de l'adresse
   * @param localite localité de l'adresse
   * @param rue rue de l'adresse
   * @param num numéro de l'adresse
   * @param tel téléphone du client
   */
  public ClientDB(int idclient,String nom,String prenom,int cp,
    String localite,String rue,String num,String tel){	
      super(idclient,nom,prenom,cp,localite,rue,num,tel);	
  }     


  /**
   * constructeur à utiliser lorsque le client est déjà présent en base de données
   * mais qu'on ne connaît que son identifiant, à utiliser avec read
   * @see #read()
   * @param idclient
   */
  public ClientDB(int idclient){
      super(idclient,"","",0,"","","","");
   }

  /**
   * méthode statique permettant de partager la connexion entre toutes les instances de
   * ClientDB
   * @param nouvdbConnect connexion à la base de données
   */
   public static void setConnection(Connection nouvdbConnect) {
      dbConnect=nouvdbConnect;
   }

  /**
   * enregistrement d'un nouveau client dans la base de données
   * @throws Exception erreur de création
   */
   public void create() throws Exception{
        CallableStatement   cstmt=null;
       try{
	     String req = "call createcli(?,?,?,?,?,?,?,?)";
	     cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
             cstmt.setString(2,nom);
	     cstmt.setString(3,prenom);
	     cstmt.setInt(4,cp);
	     cstmt.setString(5,localite);
	     cstmt.setString(6,rue);
	     cstmt.setString(7,num);
	     cstmt.setString(8,tel);
	     cstmt.executeUpdate();
             this.idclient=cstmt.getInt(1);
	    
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
 * récupération des données d'un client sur base de son identifiant
 * @throws Exception code inconnu
 */
   public void read ()throws Exception{
	
	String req = "{?=call readcli(?)}";
        
        CallableStatement cstmt=null;
        try{
        cstmt=dbConnect.prepareCall(req);
        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	cstmt.setInt(2,idclient);
	cstmt.executeQuery();
        ResultSet rs=(ResultSet)cstmt.getObject(1);
        if(rs.next()){
	     	this.nom=rs.getString("NOM");
	     	this.prenom=rs.getString("PRENOM");
	     	this.cp=rs.getInt("CP");
	     	this.localite=rs.getString("LOCALITE");
	     	this.rue=rs.getString("RUE");
	     	this.num=rs.getString("NUM");
	     	this.tel=rs.getString("TEL");
	        
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
 * méthode statique permettant de récupérer tous les clients portant un certain nom
 * @param nomrech nom recherché
 * @return liste de clients
 * @throws Exception nom inconnu
 */
   public static ArrayList<ClientDB> rechNom(String nomrech)throws Exception{
	    ArrayList<ClientDB> plusieurs=new ArrayList<ClientDB>();
	    String req = "{?=call readclinom(?)}";
            
	    CallableStatement cstmt=null;
	    try{
	    cstmt = dbConnect.prepareCall(req);
             cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	    cstmt.setString(2,nomrech);
	    cstmt.executeQuery();
            ResultSet rs=(ResultSet)cstmt.getObject(1);
	    boolean trouve=false;
            while(rs.next()){
                trouve=true;
	        int idclient=rs.getInt("IDCLIENT");
	     	String nom=rs.getString("NOM");
	     	String prenom=rs.getString("PRENOM");
	     	int cp=rs.getInt("CP");
	     	String localite=rs.getString("LOCALITE");
	     	String rue=rs.getString("RUE");
	     	String num=rs.getString("NUM");
	     	String tel=rs.getString("TEL");
	     	plusieurs.add(new ClientDB(idclient,nom,prenom,cp,localite,rue,num,tel));
	      }
	   
              if(!trouve)throw new Exception("nom inconnu");
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
      * mise à jour des données du client sur base de son identifiant
      * @throws Exception erreur de mise à jour
      */

    public void update() throws Exception{
        CallableStatement cstmt=null;

    try{
	     String req = "call updatecli(?,?,?,?,?,?,?,?)";
	     cstmt=dbConnect.prepareCall(req);
             PreparedStatement pstm = dbConnect.prepareStatement(req);
	     cstmt.setInt(1,idclient);
	     cstmt.setString(2,nom);
	     cstmt.setString(3,prenom);
	     cstmt.setInt(4,cp);
	     cstmt.setString(5,localite);
	     cstmt.setString(6,rue);
	     cstmt.setString(7,num);
	     cstmt.setString(8,tel);
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
  * effacement du client sur base de son identifiant
  * @throws Exception erreur d'effacement
  */
    public void delete()throws Exception{
	
           CallableStatement cstmt =null;
	   try{
	     String req = "call delcli(?)";
	     cstmt = dbConnect.prepareCall(req);
	     cstmt.setInt(1,idclient);
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
