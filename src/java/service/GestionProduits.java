package service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import magasin.modele.ListeProduits;
import magasin.modele.ProduitDB;


public class GestionProduits {
           
          @Path("create")
          @Consumes("application/xml")
          @PUT
          public Response create(ProduitDB pr){
            
              Response res=null;
             try {
                  pr.create();
                  res= Response.status(Response.Status.OK).build();
              }
                    
              catch(Exception e){
                   res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
         @Path("read-{numprod}")
	 @GET
	  @Produces("application/xml")
	  public ProduitDB read(@PathParam("numprod") String numprod) {
	    ProduitDB p=new ProduitDB(numprod);
		 try{
		   p.read();
		 }
		 catch(Exception e){
	           p=new ProduitDB("000000",e.getMessage(),0,0);
		 }
	      return p;
	  }
	  
          @Path("delete-{numprod}")
          @Consumes("application/xml")
          @DELETE
          public Response delete(@PathParam("numprod") String numprod){
              
              Response res=null;
              ProduitDB p=new ProduitDB(numprod);
             try {
                  p.delete();
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
          public Response update (ProduitDB p ){
              
              Response res=null;
              
             try {
                  p.update();
                  res= Response.status(Response.Status.OK).build();
              }
                    
              catch(Exception e){
                  res= Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
          @Path("search-{description}")
	 @GET
	  @Produces("application/xml")
	  public ListeProduits getListe(@PathParam("description") String description) {
	       ListeProduits lp = new ListeProduits(); 
              try{
		    lp.setListe(ProduitDB.rechProduit(description));
		 }
		 catch(Exception e){
	          
		 }
	      return lp;
	  }
          

}
