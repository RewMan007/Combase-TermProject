<%-- 
    Document   : addtoshoppingcart_result
    Created on : Oct 14, 2023, 4:11:07 PM
    Author     : ratchaphum
--%>

<%@page import="controller.Product_Controller"%>
<%@page import="controller.Product_ShoppingCart_Table"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DVD Shopping</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <body>
        <center>
            <h1>Shopping Cart</h1>
            <form name="secondForm", action="addToShoppingCartController" method="POST">
                <table >
                    <tr>
                        <th style="width:30%">DVD Names</th>
                        <th style="width:20%">Rating</th>
                        <th style="width:20%">Year</th>
                        <th style="width:10%">Price/Unit</th>
                        <th style="width:10%">Quantity</th>
                        <th style="width:10%">Price</th>
                    </tr>
                    <jsp:useBean id="products" class="controller.Product_Controller" scope="page"/>
                    <%
                        List<Product_Controller> prodList = Product_ShoppingCart_Table.getAllProduct();
                        Iterator<Product_Controller> itr = prodList.iterator();
                        while(itr.hasNext()) {
                            products = itr.next();
                            if(request.getAttribute(products.getMovie() + "_ChB") != null){
                                out.println("<tr>");
                                out.println("   <td>" + products.getMovie() + "</td>");
                                out.println("   <td style=\"text-align:center\">" + products.getRating() + "</td>");
                                out.println("   <td style=\"text-align:center\">" + products.getYearcreate() + "</td>");
                                out.println("   <td style=\"text-align:center\">" + products.getPrice() + "</td>");
                                out.println("   <td style=\"text-align:center\">" + request.getAttribute(products.getMovie() + "_Qtt") + "</td>");
                                out.println("   <td style=\"text-align:right\">" + request.getAttribute(products.getMovie() + "_TotalPricePerMovie") + "</td>");
                                out.println("</tr>");
                            }
                        }
                    %>
                    <tr>
                        <td colspan="5" style="text-align:center">Total</td>
                        <%
                            out.println("<td style=\"text-align:right\">" + request.getAttribute("totalPrice") + "</td>");
                        %>
                    </tr>
                </table>
                    <br> <input  type="submit" value="Check out" name="checkOutBTN" /> 
            </form>
        </center>
    </body>
</html>
