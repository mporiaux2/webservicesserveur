
package service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import magasin.modele.ComFactDB;
import magasin.modele.ComporteDB;
import magasin.modele.ListeComFacts;
import magasin.modele.ListeComporte;


public class GestionCommandes {
   @Path("create")
          @Consumes("application/xml")
          @PUT
          public Response create(ComFactDB cm){
                 Response res=null;
             try {
                  cm.create();
                  res= Response.status(Response.Status.OK).header("numcommande",cm.getNumcommande()).build();
              }
                    
              catch(Exception e){
                    res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
         @Path("read-{numcommande}")
	 @GET
	  @Produces("application/xml")
	  public ComFactDB read(@PathParam("numcommande") int numcommande) {
	    ComFactDB cm=new ComFactDB(numcommande);
		 try{
                  System.out.println("commande recherchée :"+numcommande);
		   cm.read();
		 }
		 catch(Exception e){
                     System.out.println("erreur !!!!!!"+e);
	           cm=new ComFactDB(0);
		 }
	      return cm;
	  }
	  
             @Path("readfact-{numfact}")
	 @GET
	  @Produces("application/xml")
	  public ComFactDB readfact(@PathParam("numfact") int numfact) {
	    ComFactDB cm=null;
		 try{
		   cm=ComFactDB.rechComFact2(numfact);
		 }
		 catch(Exception e){
	           cm=new ComFactDB(0);
		 }
	      return cm;
	  }
          
          
          @Path("delete-{numcommande}")
          @Consumes("application/xml")
          @DELETE
          public Response delete(@PathParam("numcommande") int numcommande){
              
              Response res=null;
              ComFactDB cm=new ComFactDB(numcommande);
             try {
                  cm.delete();
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
          public Response update (ComFactDB cm){
              
              Response res=null;
              
             try {
                  cm.update();
                  res= Response.status(Response.Status.OK).build();
              }
                    
              catch(Exception e){
                   res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
          @Path("search-{idclient}")
	 @GET
	  @Produces("application/xml")
	  public ListeComFacts getListe(@PathParam("idclient") int idclient) {
	       ListeComFacts lc = new ListeComFacts(); 
              try{
		    lc.setListe(ComFactDB.rechComFact1(idclient));
		 }
		 catch(Exception e){
	          
		 }
	      return lc;
	  }
          
         @Path("detail-{numcommande}")
	 @GET
	  @Produces("application/xml")
	  public ListeComporte getDetail(@PathParam("numcommande") int numcommande) {
	      ListeComporte lcp = new ListeComporte(); 
              System.out.println("recherche de "+numcommande);
              try{
		    lcp.setListe(ComporteDB.rechLignes(numcommande));
		 }
		 catch(Exception e){
	                    System.out.println("erreur !!!!!!"+e);
		 }
	      return lcp;
	  }
          
          
    }
    
    
    
    
    
