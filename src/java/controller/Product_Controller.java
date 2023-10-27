/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author ratchaphum
 */
public class Product_Controller {
    int id;
    String movie;
    String rating;
    String yearcreate;
    int price;

    public Product_Controller(){
        
    }
    
    public Product_Controller(int id, String movie, String rating, String yearcreate, int price) {
        this.id = id;
        this.movie = movie;
        this.rating = rating;
        this.yearcreate = yearcreate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getMovie() {
        return movie;
    }

    public String getRating() {
        return rating;
    }

    public String getYearcreate() {
        return yearcreate;
    }

    public int getPrice() {
        return price;
    }
    
}
