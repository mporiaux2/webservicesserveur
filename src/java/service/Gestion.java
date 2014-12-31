package service;


import java.sql.Connection;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import magasin.modele.ListeProduits;
import magasin.modele.ProduitDB;
import myconnections.DBConnection;

@Path("/gestionproduit")
public class Gestion {
    
    static{
        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.out.println("connection invalide");
        }
        else {
            System.out.println("connexion établie");
            ProduitDB.setConnection(dbConnect);
        }
    }
	 /*@Context
	 UriInfo uriInfo;
	 @Context
	 Request request;*/
	
          @GET
          @Path("accueil")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String getHello() {
         
	    return "hello"; 
	  }
	 
          @Path("oneproduct")
          @GET
          @Produces(MediaType.TEXT_XML)
          public ProduitDB getOneProduct(){
              ProduitDB p=new ProduitDB("P00001");
              try {
                  p.read();     
              
              }
                    
              catch(Exception e){
                 System.out.println("produit inconnu" + e);
              }
              return p;
          } 
          
          @Path("create")
          @Consumes("application/xml")
          @PUT
          public Response create(ProduitDB pr){
              System.out.println("appel de create");
              Response res=null;
             try {
                  pr.create();
                  res= Response.status(Response.Status.CREATED).build();
              }
                    
              catch(Exception e){
                  System.out.println("erreur"+ e);
                  res= Response.status(Response.Status.CONFLICT).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
         @Path("read-{numprod}")
	 @GET
	  @Produces("application/xml")
	  public ProduitDB getProduit(@PathParam("numprod") String numprod) {
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
                  res= Response.status(Response.Status.FORBIDDEN).header("erreur",e.getMessage()).build();
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
                  res= Response.status(Response.Status.FORBIDDEN).header("erreur",e.getMessage()).build();
              }
              return res;
          } 
          
          @Path("search-{description}")
	 @GET
	  @Produces("application/xml")
	  public ListeProduits getListeProduit(@PathParam("description") String description) {
	       ListeProduits lp = new ListeProduits(); 
              try{
		    lp.setListe(ProduitDB.rechProduit(description));
		 }
		 catch(Exception e){
	          
		 }
	      return lp;
	  }
          
/*	 @GET
	  @Produces(MediaType.TEXT_XML)
	  public List<PizzaDB> getPizzasBrowser() {
         ArrayList<PizzaDB>pizzasl=new ArrayList<PizzaDB>();
		 HashSet<PizzaDB> pizzash = BaseDonnees.getPizzas();
		 for(PizzaDB pz:pizzash){
			 pizzasl.add(pz);
		 }
	    return pizzasl; 
	  ¨*/
	 
/*	 @Path("listepizzas")
	 @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public ListePizzas getPizzas() {
		 ListePizzas lpz=new ListePizzas();
		 return lpz;
	  }
	  */
/*	 @Path("pizza-{id}")
	 @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public Pizza getPizza(@PathParam("id") int id) {
	    Pizza pz=null;
		 try{
		 pz = BaseDonnees.getPizza(id);
		 }
		 catch(Exception e){
			 return new Pizza(0,"inconnue",0);
		 }
	    
	    return pz;
	  }*/
	  
/*	 @Path("pizza-{id}")
	 @GET
	  @Produces({MediaType.TEXT_XML})
	  public Pizza getPizzaBrowser(@PathParam("id") int id) {
	    Pizza pz=null;
		 try{
		 pz = BaseDonnees.getPizza(id);
		 }
		 catch(Exception e){
			 System.out.println("ereur "+e);
			 return new Pizza(0,"inconnue",0);
		 }
	    
	    return pz;
	  }
	 */
	 
/*	 @Path("commande-{id}")
	 @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public Commande getCommande(@PathParam("id") int id) {
	    Commande cm=null;
		 try{
		 cm = BaseDonnees.getCommande(id);
		 }
		 catch(Exception e){
			 return new Commande(0,"inconnue","inconnu");
		 }
	  	    return cm;
	  }
	  
	 @Path("commande-{id}")
	 @GET
	  @Produces({MediaType.TEXT_XML})
	  public Commande getCommandeXML(@PathParam("id") int id) {
	    Commande cm=null;
		 try{
		 cm = BaseDonnees.getCommande(id);
		 }
		 catch(Exception e){
			 System.out.println("ereur "+e);
			 return new Commande(0,"inconnue","inconnu");
		 }
	  	    return cm;
	  }
          
          
         
          
         @Path("newpizza")
         @Consumes("application/xml")
	 @PUT
	
	  public Response  addPizza(PizzaDB pz)  {
	            System.out.println("pizza reçue = "+pz);
                   // Response res=Response.created(uriInfo.getAbsolutePath()).build();
                     Response res= Response.status(Response.Status.OK).build();
                    return res;
	  }
	 
          @Path("onepizza")
          @GET
          @Produces(MediaType.TEXT_XML)
          public PizzaDB getOnePizza(){
              PizzaDB pz=new PizzaDB("orange",4);
              return pz;
          }
          
          
	/* @Path("newcommande")
	 @GET
	 @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public Commande getNewCommande() {
	    Commande cm=new Commande("","");
	    BaseDonnees.addCommande(cm);
	    return cm;
	  }
	  */
	/* 
	 @POST
	 @Path("valider")
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response putCommande(String cmjson) {
	    System.out.println("reception de "+cmjson);
	    Gson gson=new Gson();
	    Commande cm= gson.fromJson(cmjson,Commande.class);
	    BaseDonnees.addCommande(cm);
	    return Response.status(Response.Status.OK).header("numero",""+cm.getId()).build();
	  }*/
  
}
