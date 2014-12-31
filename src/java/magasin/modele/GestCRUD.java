/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package magasin.modele;

/**
 *
 * @author Michel
 */
public class GestCRUD {
    public static void create(CRUD instance) throws Exception{
        instance.create();
    }
    
    public static void read(CRUD instance)throws Exception{
        instance.read();
    }
    
    public static void update(CRUD instance)throws Exception {
        instance.update();
    }
    public static void delete(CRUD instance)throws Exception {
        instance.update();
    }
      
}
