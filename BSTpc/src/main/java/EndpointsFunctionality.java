import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.common.io.Files;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EndpointsFunctionality {

    public String accountCreation(Request request, Response response){
        boolean found = false;
        String userName = request.queryParams("username");
        String password = request.queryParams("password");
        String password2 = request.queryParams("confirm_password");
        String email = request.queryParams("email");
        String first = request.queryParams("fName");
        String last = request.queryParams("lName");
        User user = new User(userName, first, last,email);
        int i = user.hashCode();
        Connection con;
        response.type("text/html");
        String insert = "INSERT into TPC_DB.users " + "(uName, pass, email, firstName, lastName, secretKey)" + "VALUES " + "('" + userName + "','"
                + password + "','" + email + "','" + first + "','" + last + "','"+ i + "')";
        String lookup = "SELECT * FROM TPC_DB.users WHERE uName = '" + userName + "'";
        Statement statement = null;
        ResultSet result;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
            statement = con.createStatement();
            result = statement.executeQuery(lookup);
            while (result.next()) {
                String uName = result.getString("uName");
                if (userName.contentEquals(uName)) {
                    found = true;
                    response.redirect("/Register");
                }
            }
        } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
        if (userName == null || "".equals(userName) || password == null || "".equals(password) || password2 == null
                || "".equals(password2))
            response.redirect("/Register");
         else if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?"
                + "^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
                + "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
                + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)) {
            response.redirect("/Register");
        } else if (password == null || "".equals(password) || !password.equals(password2)) {
            response.redirect("/Register");
        } else if(!found){
            try {
                if (statement != null) {
                    String secretKey = "";
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
                        statement = con.createStatement();
                        result = statement.executeQuery(lookup);
                        while (result.next()) {
                            String uName = result.getString("uName");
                            if (userName.contentEquals(uName)) {
                                secretKey = result.getString("secretKey");
                            }
                        }
                    } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
                    statement.executeUpdate(insert);
                    request.session().attribute("uName", userName);
                    request.session().maxInactiveInterval(99999);
                    request.session().attribute("uEmail", email);
                    request.session().maxInactiveInterval(99999);
                    request.session().attribute("secretKey", secretKey);
                    request.session().maxInactiveInterval(99999);
                    response.redirect("/Home");
                    return "";
                } else
                    response.redirect("/Register");
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.redirect("/Register");
        return "";
    }

    public String validatePart(Request request, Response response){
        String s = request.session().attribute("uName");
        String partName = request.queryParams("partName");
        String price = request.queryParams("price");
        String description = request.queryParams("description");
        String condition = request.queryParams("condition");
        String category= request.queryParams("category");
        Connection con;
        String insert = "INSERT into TPC_DB.auctioned " + "(uName, partName, description, price, partCondition, category)" + "VALUES " + "('" +s+ "','"
                + partName + "','" + description + "','" + price + "','" + condition + "','" + category +"')";
        String lookup = "SELECT * FROM TPC_DB.auctioned WHERE uName = '" + s + "' AND partName = '" + partName + "'" ;
        Statement statement = null;
        ResultSet result = null;
        response.type("text/html");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
            statement = con.createStatement();
            result = statement.executeQuery(lookup);
            while (result.next()) {
                String uName = result.getString("uName");
                if (uName.contentEquals(s)) response.redirect("/Home");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (s == null || "".equals(s) || partName == null || "".equals(partName) || price == null
                || "".equals(price) || description == null || "".equals(description) || condition == null ||
                "".equals(condition) || category == null || "".equals(category))
            response.redirect("/Home");
         else try {
                if (statement != null && !partName.contains("_")) {
                    statement.executeUpdate(insert);
                    response.redirect("/Home");
                    return "";
                } else
                    response.redirect("/Home");
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        response.redirect("/Home");
        return "";
    }

    public String loginVerification(Request request, Response response){
        String userName = request.queryParams("username");
        String password = request.queryParams("password");
        Connection con;
        ResultSet result;
        String lookup = "SELECT * FROM TPC_DB.users WHERE uName = '" + userName + "' AND pass = '"
                + password + "';";
        Statement statement;
        ResultSet results;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
            statement = con.createStatement();
            results = statement.executeQuery(lookup);
            while (results.next()) {
                String uName = results.getString("uName");
                String pass = results.getString("pass");
                String email = results.getString("email");
                if (userName.contentEquals(uName) && password.contentEquals(pass)) {
                    String secretKey = "";
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
                        statement = con.createStatement();
                        result = statement.executeQuery(lookup);
                        while (result.next()) {
                            if (userName.contentEquals(uName)) {
                                secretKey = result.getString("secretKey");
                            }
                        }
                    } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
                    request.session().attribute("uName", userName);
                    request.session().maxInactiveInterval(99999);
                    request.session().attribute("uEmail", email);
                    request.session().maxInactiveInterval(99999);
                    request.session().attribute("secretKey", secretKey);
                    request.session().maxInactiveInterval(99999);
                    response.redirect("/Home");
                    return "";
                }
            }
            response.redirect("/Login");
            return "";
        } catch (SQLException | ClassNotFoundException e) {
            response.redirect("/Login");
            return "";
        }
    }

    public String home(Request request, Response response) throws IOException {
        String s = request.session().attribute("uName");
        response.type("text/html");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/Home.html"), StandardCharsets.UTF_8).read();
        String output ="";
        if(s != null) output+= "            <a id=\"header\" href=\"/AddPart\">Add Part</a>\n" +
                "<a href=\"/logout\"id =\"header-right\">Logout</a>" +
                                "<a id =\"header-right\">Hello, "+s+"</a>";
        else output+="            <a id=\"header-right\" href=\"/Login\">Login</a>\n" +
                        "            <a id=\"header-right\" href=\"/Register\">Register</a>\n";

        return content.replace("{AddHere}",output);
    }

    public String login(Request request, Response response) throws IOException {
        if(request.session().attribute("uName") != null) response.redirect("/home");
        response.type("text/html");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/Login.html"), StandardCharsets.UTF_8).read();
        return content;
    }

    public String addPart(Request request, Response response) throws IOException {
        if(request.session().attribute("uName") == null) response.redirect("/Home");
        response.type("text/html");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/AddPart.html"), StandardCharsets.UTF_8).read();
        return content;
    }

    public String register(Request request, Response response) throws IOException {
        if(request.session().attribute("uName") != null) response.redirect("/home");
        response.type("text/html");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/Register.html"), StandardCharsets.UTF_8).read();
        return content;
    }

    public String search(Request request, Response response) throws IOException {
        if(request.session().attribute("uName") == null) response.redirect("/home");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/Search.html"), StandardCharsets.UTF_8).read();
        String user = request.session().attribute("uName");
        content = content.replace("{s}", user);
        response.type("text/html");
        String output = "";
        String category = request.queryParams("type");
            Connection con;
            String lookup =
                    "SELECT auctioned.*, users.email, users.secretKey " +
                    "FROM auctioned, users " +
                    "WHERE auctioned.category = '"+ category +"' AND auctioned.uName = users.uName;";
            Statement statement;
            ResultSet results;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
                statement = con.createStatement();
                results = statement.executeQuery(lookup);
                while (results.next()) {
                    NumberFormat format = NumberFormat.getInstance();
                    format.setGroupingUsed(true);
                    double price = Double.parseDouble(results.getString("price"));
                    String to = results.getString("email");
                    String itemName = results.getString("partName");
                    String buyer = user;
                    String seller = results.getString("uName");
                    String redirect = "/Email?to="+to+"&part="+itemName+"&buyer="+buyer+"&seller="+seller;
                    output += "<div class=\"column\">\n" +
                            "                        <div class=\"card\">\n" +
                            "                        <h3 id=\"card-title\">"+ results.getString("partName")+ "</h3>\n" +
                            "                        <div class=\"inputs\">\n" +
                            "                        <p>\n" +
                            "                            <label for=\"price\" id=\"label\">Price: $"+ format.format(price) +"</label><hr/>\n" +
                            "                            <label for=\"condition\" id=\"label\">Condition: "+ results.getString("partCondition") +"</label><hr/>\n" +
                            "                            <label for=\"description\" id=\"label\">About: </label>\n" +
                            "                            <input type=\"text\" id=\"description\" value=\""+results.getString("description")+"\" readonly></input>\n" +
                            "                            <hr/>\n" +
                            "                            <br/>\n" +
                            "                            <button value=\"trade\" onclick=\"window.location.href='"+redirect+"'\" id=\"trade-button\" {activated}>Buy</button>\n" +
                            "                        </p>" +
                            "                        </div>\n" +
                            "                        </div>\n" +
                            "                    </div>";
                    if(buyer.equals(seller)){
                        output = output.replace("{activated}", "disabled");
                    } else {
                        output = output.replace("{activated}", "");
                    }
                }

            } catch (SQLException | ClassNotFoundException e) {
                response.redirect("/Home");
                return "";
            }
        return content.replace("{InsertHere}", output);
    }
    
    public String searchByName(Request request, Response response) throws IOException {
        if(request.session().attribute("uName") == null) response.redirect("/home");
        String content = Files.asCharSource(new File("/home/andrew/IdeaProjects/BSTpc/src/main/resources/HTML/Search.html"), StandardCharsets.UTF_8).read();
        String user = request.session().attribute("uName");
        content = content.replace("{s}", user);
        response.type("text/html");
        String output = "";
        String name = request.queryParams("search");
        Connection con;
        String lookup =
                "SELECT auctioned.*, users.email, users.secretKey " +
                "FROM auctioned, users " +
                "WHERE auctioned.uName = users.uName AND auctioned.partName LIKE '%"+name+"%';";
        Statement statement;
        ResultSet results;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
            statement = con.createStatement();
            results = statement.executeQuery(lookup);
            while (results.next()) {
                NumberFormat format = NumberFormat.getInstance();
                format.setGroupingUsed(true);
                double price = Double.parseDouble(results.getString("price"));
                String to = results.getString("email");
                String itemName = results.getString("partName");
                String buyer = user;
                String seller = results.getString("uName");
                String redirect = "/Email?to="+to+"&part="+itemName+"&buyer="+buyer+"&seller="+seller;
                output += "<div class=\"column\">\n" +
                        "                        <div class=\"card\">\n" +
                        "                        <h3 id=\"card-title\">"+ results.getString("partName")+ "</h3>\n" +
                        "                        <div class=\"inputs\">\n" +
                        "                        <p>\n" +
                        "                            <label for=\"price\" id=\"label\">Price: $"+ format.format(price) +"</label><hr/>\n" +
                        "                            <label for=\"condition\" id=\"label\">Condition: "+ results.getString("partCondition") +"</label><hr/>\n" +
                        "                            <label for=\"description\" id=\"label\">About: </label>\n" +
                        "                            <input type=\"text\" id=\"description\" value=\""+results.getString("description")+"\" readonly></input>\n" +
                        "                            <hr/>\n" +
                        "                            <br/>\n" +
                        "                            <button value=\"trade\" onclick=\"window.location.href='"+redirect+"'\" id=\"trade-button\" {activated}>Buy</button>\n" +
                        "                        </p>\n" +
                        "                        </div>\n" +
                        "                        </div>\n" +
                        "                    </div>";
                if(buyer.equals(seller)){
                    output = output.replace("{activated}", "disabled");
                } else {
                    output = output.replace("{activated}", "");
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            response.redirect("/Home");
            return "";
        }
        return content.replace("{InsertHere}", output);
    }

    public String logout(Request request, Response response){
        request.session().attribute("uName", null);
        response.redirect("/Home");
        return "";
    }

    public String email(Request request, Response response){
        if(request.session().attribute("uName") == null) response.redirect("/home");
        String to = request.queryParams("to");
        String itemName = request.queryParams("part");
        String buyer = request.queryParams("buyer");
        String seller = request.queryParams("seller");
        String from = "******";
        String pass ="******";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties);
        Connection con;
        String delete ="DELETE FROM auctioned WHERE uName=\""+seller+"\" AND partname=\""+itemName+"\";\n";
        Statement statement;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu/TPC_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=aboyce3&password=mysql");
            statement = con.createStatement();
            statement.executeUpdate(delete);
        } catch (SQLException | ClassNotFoundException e) {
            response.redirect("/Home");
            return "";
        }
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Request to buy/trade your " + itemName);
            message.setText("Hello "+ seller+",\n\nWe are delighted to inform you that " + buyer + " has purchased your \""
                    + itemName + "\". If you have any questions then reply to this email and we will get back to you as soon as possible.\n\n" +
                    "-BottyBoi");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
            response.redirect("/Home");
        }
        response.redirect("/Home");
        return "";
    }

}
