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

/**
 *
 * @author ratchaphum
 */
public class catalogController extends HttpServlet {

    //จัดการเรื่อง session และ การส่งข้ามหน้า
    
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

        //set every session attritube ที่เป็นปุ่ม (contains _ChB) to false and attrs ที่เป็นค่าจำนวนหนัง(contains _Qtt) to -1
        Enumeration<String> session_attrs = session.getAttributeNames();
        while(session_attrs.hasMoreElements()){        
            String single_session_attr = session_attrs.nextElement();
            if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_ChB")){   //this session attr is movie_CheckBox
                session.setAttribute(single_session_attr, false);
                //เซ็ตทุกค่าเป็น false ไว้ก่อน 
            }else if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_Qtt")){ //this session attr is movie_Quantity
                session.setAttribute(single_session_attr, "-1");
            }
        }

        //update session attrs ให้ match กับ request params (ค่าที่เราใส่ในตาราง)
        Enumeration<String> req_params = request.getParameterNames();
        while(req_params.hasMoreElements()){             
            String single_req_param = req_params.nextElement();
            if(single_req_param.contains("_ChB")){  //this request param is movie_CheckBox ถ้ามีเป็น true
                session.setAttribute(single_req_param, true);
            }else if(single_req_param.contains("_Qtt")){    //this request param is movie_Quantity ถ้ามีเป็นค่าเดียวกะ single_req_param
                session.setAttribute(single_req_param, request.getParameter(single_req_param));
            }
        }

        //set request attrs ที่ใช้ and delete (req_attrs and sess_attrs) ที่ไม่ใช้
        Enumeration<String> session_attrs2 = session.getAttributeNames();
        int totalPrice = 0;
        while(session_attrs2.hasMoreElements()){    
            String single_session_attr = session_attrs2.nextElement();
            if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_ChB")){
                if((boolean)session.getAttribute(single_session_attr) == true){
                    request.setAttribute(single_session_attr, true);
                }else{
                    session.removeAttribute(single_session_attr); //removeAttribute จาก session
                    request.removeAttribute(single_session_attr); //removeAttribute จาก request
                }
            }else if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_Qtt")){
                //มีค่า != -1 ไหม
                if(!((String)session.getAttribute(single_session_attr)).equals("-1")){
                    request.setAttribute(single_session_attr, (String)session.getAttribute(single_session_attr));
                    
                    //จำนวน
                    int quantity = Integer.valueOf((String)session.getAttribute(single_session_attr));
                    //ราคาต่อชิ้น
                    int pricePerUnit = Product_ShoppingCart_Table.findProductByMovie(single_session_attr.replace("_Qtt", "")).getPrice();
                    //ราคารวมของหนังหนึ่งเรื่อง
                    int totalPricePerMovie = Product_ShoppingCart_Table.calculatePrice(quantity, pricePerUnit);
                    
                    // setAttribute ราคารวมของหนังหนึ่งเรื่อง
                    request.setAttribute(single_session_attr.replace("_Qtt", "") + "_TotalPricePerMovie", totalPricePerMovie);
                    
                    //ราคารวมทั้งหมด
                    totalPrice += totalPricePerMovie;
                    
                }else{
                    session.removeAttribute(single_session_attr);
                    request.removeAttribute(single_session_attr);
                }
            }
        }
        
        //set Attribute totalPrice ให้ session และ request
        session.setAttribute("totalPrice", totalPrice);
        request.setAttribute("totalPrice", totalPrice);
        
        request.getRequestDispatcher("addToShoppingCart.jsp").forward(request, response);
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
