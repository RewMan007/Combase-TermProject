<%-- 
    Document   : addtoshoppingcart_result
    Created on : Oct 14, 2023, 4:09:55 PM
    Author     : ratchaphum
--%>

<%@page import="controller.Product_Controller"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="controller.Product_ShoppingCart_Table"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DVD Shopping</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <body>
        <script>
            // ฟังก์ชัน enableSubmitBtn() ใช้เพื่อตรวจสอบว่าปุ่ม "Add To Cart" จะถูก disabled หรือไม่
            function enableSubmitBtn(){
                console.log("run enableSubmitBtn()");
                var idList = document.querySelectorAll("[id]"); // เลือกทุก element ที่มี attribute "id" และจัดเก็บในตัวแปร idList
                var isDisable = true;
                for(let i = 0; i < idList.length; i++){
                    if(document.getElementById(idList[i].id).checked && document.getElementById(idList[i].id + "_Qtt").value !== ""){
                        isDisable = false;
                        // ถ้ามีอิลิเมนต์ที่ถูกเลือกและมีค่าใน input จำนวนสินค้า แล้ว isDisable จะกลายเป็น false -> กดปุ่มได้
                    }else if(document.getElementById(idList[i].id).checked && document.getElementById(idList[i].id + "_Qtt").value === ""){
                        isDisable = true;
                        break;
                        // ถ้ามีอิลิเมนต์ที่ถูกเลือก แต่ไม่มีค่าใน input จำนวนสินค้า แล้ว isDisable จะกลายเป็น true และ break เลย -> กดปุ่มไม่ได้
                    }
                }
                document.getElementById("submit").disabled = isDisable; //ปุ่ม "Add To Cart" จะถูก disabled หรือไม่ ขึ้นอยู่กับ isDisable
            }

            // ฟังก์ชัน enableTextInput() ใช้เพื่อเปิดกล่องป้อนข้อมูลจำนวนสินค้าเมื่อมี DVD ที่ถูกเลือกและปิดการป้อนข้อมูลเมื่อไม่มี DVD ที่ถูกเลือก
            function enableTextInput(){
                var idList = document.querySelectorAll("[id]"); // เลือกทุกอิลิเมนต์ที่มี attribute "id" และจัดเก็บในตัวแปร idList
                console.log(idList);
                for(let i = 0; i < idList.length; i++){
                    if(document.getElementById(idList[i].id).checked){
                        document.getElementById(idList[i].id + "_Qtt").disabled = false;  // เปิดใช้งาน input จำนวนสินค้า เมื่อ DVD ถูกเลือก
                        //ไม่โดน check
                    }else if (!document.getElementById(idList[i].id).checked && !idList[i].id.includes("_Qtt") && idList[i].id !== "submit"){
                        document.getElementById(idList[i].id + "_Qtt").value = "";
                        document.getElementById(idList[i].id + "_Qtt").disabled = true;
                    }
                }
                enableSubmitBtn();
            }
        </script>
        <center>
            <h1>DVD Catalog</h1>
            <form name="firstForm" action="catalogController" method="POST">
                <!-- form เรียก action catalogController  -->
                <table border="1" style="width:40%" >
                    <tr>
                        <th style="width:40%">DVD Names</th>
                        <th style="width:20%">Rating</th>
                        <th style="width:20%">Year</th>
                        <th style="width:10%">Price</th>
                        <th style="width:10%">Quantity</th>
                    </tr>
                    <jsp:useBean id="products" class="controller.Product_Controller" scope="page"/>
                    <%
                        List<Product_Controller> prodList = Product_ShoppingCart_Table.getAllProduct(); //ทำ list ของ prodect controller
                        Iterator<Product_Controller> itr = prodList.iterator();
                        while(itr.hasNext()) {
                            products = itr.next();
                            out.println("<tr>");
                            out.println("   <td> <input type=\"checkbox\"" 
                                         + "name=\"" + products.getMovie()
                                         + "_ChB\" value=\"checked\""
                                         + "id=\"" + products.getMovie()
                                         + "\" onchange=\"enableTextInput()\" />" + products.getMovie() + "</td>");
                            out.println("   <td style=\"text-align:center\"> "+ products.getRating() + "</td>");
                            out.println("   <td style=\"text-align:center\"> "+ products.getYearcreate() + "</td>");
                            out.println("   <td style=\"text-align:center\"> "+ products.getPrice() + "</td>");
                            out.println("   <td> <input type=\"text\" name=\"" + products.getMovie() + "_Qtt\" value=\"\" " + "id=\"" + products.getMovie() + "_Qtt\" size=\"5\""
                                         + "style=\"text-align:right\" pattern=\"[1-9]+\" title=\"Please Enter a Number More than 1!\" onchange=\"enableSubmitBtn()\" disabled/></td>");
                            out.println("</tr>");
                        }
                        
                        
                        //เรียก session ใช้ในการจัดการเรื่อง user หลายคน
                        session = request.getSession();
                        
                        if(session.isNew()){
                            //เมื่อเป็น session ใหม่ (ของ user คนใหม่) check box ทั้งหมดจะถูกติ๊กเป็น false 
                            out.println("<script>");
                            for(Product_Controller prod : prodList){
                                out.println("document.getElementById(\"" + prod.getMovie() + "\").checked = false;");
                            }
                            out.println("</script>");
                            
                        }else {
                            //เมื่อเป็น session เดิม (ของ user คนเดิม) ...
                            out.println("<script>");
                            Enumeration<String> session_attrs = session.getAttributeNames();
                            //เอาแอทริบิวท์ใน session มาตรวจสอบทีละตัว
                            while(session_attrs.hasMoreElements()){
                                String single_session_attr = session_attrs.nextElement();
                                if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_ChB")){
                                    // ถ้ามีเซสชันอาทิตบิวต์ที่ contains "_ChB" =เก็บเป็นค่าของ checkbox 
                                    out.println("document.getElementById(\"" + single_session_attr.replace("_ChB", "") + "\").checked = " + (Boolean) session.getAttribute(single_session_attr) + ";");
                                }else if(!single_session_attr.equals("WELD_S_HASH") && single_session_attr.contains("_Qtt")){
                                    // ถ้ามีเซสชันอาทิตบิวต์ที่ contains "_Qtt" = เก็บเป็นค่าของ จำนวนสินค้า  
                                    out.println("document.getElementById(\"" + single_session_attr + "\").value = \"" + Integer.valueOf((String) session.getAttribute(single_session_attr)) + "\";");
                                }
                            }
                            out.println("</script>");
                        }
                    %>
                </table>
                    <br> <input  type="submit" value="Add To Cart" name="submitBTN" id="submit" disabled />
                    <%
                        out.println("<script>enableTextInput();</script>");
                        //เรียก enableTextInput() เพื่อทำให้ ทุกกล่อง checkbox ที่ไม่ติ๊ก ช่องกรอกปริมาณสินค้ากรอกไม่ได้
                    %>
            </form>
        </center>
    </body>
</html>
