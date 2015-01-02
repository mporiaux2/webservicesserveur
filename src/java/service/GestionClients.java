
package service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import magasin.modele.ClientDB;
import magasin.modele.ListeClients;


public class GestionClients {
   @Path("create")
          @Consumes("application/xml")
          @PUT
          public Response create(ClientDB cl){
                 Response res=null;
             try {
                  cl.create();
                  res= Response.status(Response.Status.OK).header("numcli",cl.getIdclient()).build();
              }
                    
              catch(Exception e){
                 
                 res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
         @Path("read-{numcli}")
	 @GET
	  @Produces("application/xml")
	  public ClientDB read(@PathParam("numcli") int numcli) {
	    ClientDB cl=new ClientDB(numcli);
		 try{
		   cl.read();
		 }
		 catch(Exception e){
	           cl=new ClientDB(0);
		 }
	      return cl;
	  }
	  
          @Path("delete-{numcli}")
          @Consumes("application/xml")
          @DELETE
          public Response delete(@PathParam("numcli") int numcli){
              
              Response res=null;
              ClientDB cl=new ClientDB(numcli);
             try {
                  cl.delete();
                  res= Response.status(Response.Status.OK).build();
              }
                    
              catch(Exception e){
                  res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
          @Path("update")
          @Consumes("application/xml")
          @PUT
          public Response update (ClientDB cl ){
              
              Response res=null;
              
             try {
                  cl.update();
                  res= Response.status(Response.Status.OK).build();
              }
                    
              catch(Exception e){
                   res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
          @Path("search-{nom}")
	 @GET
	  @Produces("application/xml")
	  public ListeClients getListe(@PathParam("nom") String nom) {
	       ListeClients lc = new ListeClients(); 
              try{
		    lc.setListe(ClientDB.rechNom(nom));
		 }
		 catch(Exception e){
	          
		 }
	      return lc;
	  }
    }
    
    
    
    
    
