/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Products;
import model.Shoppingcart;

/**
 *
 * @author ratchaphum
 */
public class Product_ShoppingCart_Table {
    public static List<Products> findAllProduct(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CBSD_ProjectPU");
        EntityManager em = emf.createEntityManager();
        List<Products> prodList = null;
        try{
            prodList = (List<Products>) em.createNamedQuery("Products.findAll").getResultList();
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            em.close();
            emf.close();
        }
        return prodList;
    }
    
    public static List<Product_Controller> getAllProduct(){
        List<Products> prodList = findAllProduct();
        List<Product_Controller> prod_con_List = new ArrayList<>();
        for(Products single_prod : prodList){
            prod_con_List.add(new Product_Controller(single_prod.getId(), single_prod.getMovie(), single_prod.getRating(), single_prod.getYearcreate(), single_prod.getPrice()));
        }
        return prod_con_List;
    }
    
    public static Products findProductByMovie(String movie){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CBSD_ProjectPU");
        EntityManager em = emf.createEntityManager();
        Products prod = null;
        try{
            prod = (Products) em.createNamedQuery("Products.findByMovie").setParameter("movie", movie).getSingleResult();
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            em.close();
            emf.close();
        }
        return prod;
    }
    
    public static int calculatePrice(int quantity, int pricePerUnit){
        return quantity * pricePerUnit;
    }
    
    public static int findLastestCartID(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CBSD_ProjectPU");
        EntityManager em = emf.createEntityManager();
        int lastestCartID = 0;
        List<Shoppingcart> shoppingCartList = null;
        try{
            shoppingCartList = (List<Shoppingcart>) em.createNamedQuery("Shoppingcart.findAll").getResultList();
            for(Shoppingcart spCart : shoppingCartList){
                if(spCart.getShoppingcartPK().getCartId() > lastestCartID){
                    lastestCartID = spCart.getShoppingcartPK().getCartId();
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            em.close();
            emf.close();
        }
        return lastestCartID;
    }
    
    public static void insertShoppingCart(Shoppingcart shoppingCart){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CBSD_ProjectPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(shoppingCart);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
