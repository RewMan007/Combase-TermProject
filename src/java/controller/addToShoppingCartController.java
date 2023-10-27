/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Products;
import model.Shoppingcart;

/**
 *
 * @author ratchaphum
 */
public class addToShoppingCartController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        //ถ้า ค่า totalPrice ใน session = null (กรณีเปิด 2 หน้าในเว็บบราวเซอร์ แต่จบ session)
        if(session.getAttribute("totalPrice") == null){
            request.getRequestDispatcher("err_sameSession.jsp").forward(request, response);
        }else{
            Enumeration<String> session_attrs = session.getAttributeNames();
            while(session_attrs.hasMoreElements()){
                String single_session_attr = session_attrs.nextElement();
                if(!single_session_attr.equals("WELD_S_HASH")){
                    //เอาทุก attribute ใน session ลอกค่าใส่ attribute ใน request
                    request.setAttribute(single_session_attr, session.getAttribute(single_session_attr));
                }
            }
            
            //*-------ปิด session--------*//
            session.invalidate();
            
            
            //*-------จัดการ data base--------*//
            synchronized(getServletContext()) {
                int lastestCartID = Product_ShoppingCart_Table.findLastestCartID();
                getServletContext().setAttribute("lastestCartID", lastestCartID + 1);  //set attribute lastestCartID เป็นตัวถัดไป
                Enumeration<String> req_attrs = request.getAttributeNames();
                while(req_attrs.hasMoreElements()){
                    String single_req_attr = req_attrs.nextElement();
                    Products prod = null;
                    int quantity = 0;
                    if(single_req_attr.contains("_ChB")){
                        prod = Product_ShoppingCart_Table.findProductByMovie(single_req_attr.replace("_ChB", ""));
                        quantity = Integer.valueOf((String) request.getAttribute(single_req_attr.replace("_ChB", "_Qtt")));
                        
                        // new object shopping cart  //
                        Shoppingcart shopCart = new Shoppingcart((int) getServletContext().getAttribute("lastestCartID"), prod.getId());
                        shopCart.setQuantity(quantity);
                        Product_ShoppingCart_Table.insertShoppingCart(shopCart);
                    }
                }
            }
            request.getRequestDispatcher("ShowConfirmation.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
