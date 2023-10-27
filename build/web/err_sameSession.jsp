<%-- 
    Document   : addtoshoppingcart_result
    Created on : Oct 17, 2023, 5:14:45 PM
    Author     : ratchaphum
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Same session Handling</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
    </head>
    <Script>
        function msgAlert(){
            alert("Your session has already ended!\nPlease select the product again.");
        }
    </Script>
    <body onload="msgAlert()">
        <center>            
            <jsp:include page="catalog.jsp" flush="true" />
        </center>
    </body>
</html>
