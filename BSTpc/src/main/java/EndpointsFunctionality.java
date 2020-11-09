import spark.Request;
import spark.Response;

import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EndpointsFunctionality {

    public String accountCreation(Request request, Response response){
        String userName = request.queryParams("username");
        String password = request.queryParams("password");
        String password2 = request.queryParams("confirm_password");
        String email = request.queryParams("email");
        String first = request.queryParams("fName");
        String last = request.queryParams("lName");
        Connection con;
        response.type("text/html");
        String insert = "INSERT into TPC_DB.users " + "(uName, pass, email, firstName, lastName)" + "VALUES " + "('" + userName + "','"
                + password + "','" + email + "','" + first + "','" + last + "')";
        String lookup = "SELECT * FROM TPC_DB.users WHERE uName = '" + userName + "'";
        Statement statement = null;
        ResultSet result = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/TPC_DB?user=root&password=password");
            statement = con.createStatement();
            result = statement.executeQuery(lookup);
            while (result.next()) {
                String uName = result.getString("uName");
                if (userName.contentEquals(uName))
                    response.redirect("/Register");
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
        } else {
            try {
                if (statement != null) {
                    statement.executeUpdate(insert);
                    request.session().attribute("username", userName);
                    request.session().maxInactiveInterval(9999);
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
            con = DriverManager.getConnection("jdbc:mysql://localhost/TPC_DB?user=root&password=password");
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
                if (statement != null) {
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
        Connection con = null;
        String lookup = "SELECT * FROM TPC_DB.users WHERE uName = '" + userName + "' AND pass = '"
                + password + "';";
        Statement statement = null;
        ResultSet results = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/TPC_DB?user=root&password=password");
            statement = con.createStatement();
            results = statement.executeQuery(lookup);
            while (results.next()) {
                String uName = results.getString("uName");
                String pass = results.getString("pass");
                if (userName.contentEquals(uName) && password.contentEquals(pass)) {
                    request.session().attribute("uName", userName);
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

    public String home(Request request, Response response){
        String s = request.session().attribute("uName");
        response.type("text/html");
        String output = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Welcome to BSTpc!</title>\n" +
                "        <link href=\"https://fonts.googleapis.com/css2?\n" +
                "                    family=Courier+Prime:ital,wght@0,\n" +
                "                    400;0,700;1,400;1,700&display=swap\"\n" +
                "                    rel=\"stylesheet\">\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"header\">\n" +
                "            <a href=\"home\">Home</a>\n" +
                "            <a href=\"#contact\"># Contact Us</a>\n" +
                "            <div class=\"sidenavigation\">\n" +
                "                <button class=\"drop-btn\" onclick=\"drop()\"># Categories &#9660;</button>\n" +
                "                <div class=\"drop-content\">\n" +
                "                    <a href=\"#\">CPUs / Processors</a>\n" +
                "                    <a href=\"#\">Memory</a>\n" +
                "                    <a href=\"#\">Motherboards</a>\n" +
                "                    <a href=\"#\">Video Cards</a>\n" +
                "                    <a href=\"#\">Computer Cases</a>\n" +
                "                    <a href=\"#\">Power Supplies</a>\n" +
                "                    <a href=\"#\">Fans & PC Cooling</a>\n" +
                "                    <a href=\"#\">Sound Cards</a>\n" +
                "                    <a href=\"#\">Hard Drives</a>\n" +
                "                    <a href=\"#\">SSDs</a>\n" +
                "                    <a href=\"#\">USB Flash Drives & Memory Cards</a>\n" +
                "                </div>\n" +
                "            </div>\n" ;
        if(s != null)output += "            <a id=\"header\" href=\"AddPart\">Add Part</a>\n";
        output +="            <a id=\"header-right\"";
        if(s == null) output += "href=\"login\">Login</a>\n<a id=\"header-right\" href=\"register\">Register</a>\n";
        else output += ">Hello, "+s+"</a> <a id=\"header-right\" href=\"logout\">Logout</a>";
        return output += "        <div class=\"logo\">\n" +
                "            <img src=\"logo.gif\" alt=\"TPC\">\n" +
                "        <div class=\"search-bar\">\n" +
                "            <input class=\"search\" type=\"text\" placeholder=\"Search...\" id=\"search\" name=\"search\">\n" +
                "            <input class=\"submit\"type=\"submit\" value=\"Search!\">\n" +
                "        </div>\n" +
                "        </div>\n" +
                "        <script src = \"drop.js\"></script>\n" +
                "    </body>\n" +
                "</html>\n";
    }
    public String login(Request request, Response response){
        if(request.session().attribute("uName") != null) response.redirect("/home");
        response.type("text/html");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Welcome to BSTpc!</title>\n" +
                "        <link href=\"https://fonts.googleapis.com/css2?\n" +
                "                    family=Courier+Prime:ital,wght@0,\n" +
                "                    400;0,700;1,400;1,700&display=swap\"\n" +
                "                    rel=\"stylesheet\">\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "    </head>\n" +
                "        <div class=\"logo-reg\">\n" +
                "            <img src=\"logo.gif\" alt=\"TPC\">\n" +
                "            </div>\n" +
                "<form id=\"form\" action=\"/ValidateLogin\" method=\"get\">\n" +
                "    <p>\n" +
                "        <label for=\"username\">Username: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"text\" name=\"username\" id=\"username\" required></input><br>\n" +
                "        </p>\n" +
                "    <p>\n" +
                "        <label for=\"password\">Password: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"password\" name=\"password\" id=\"password\" requried></input><br>\n" +
                "        </p>\n" +
                "        <p>\n" +
                "    <input id=\"register\" type=\"submit\" value=\"Login!\"></input> \n" +
                "        </p>\n" +
                "    <p>\n" +
                "    <br/>\n" +
                "    <a href = \"home\"> Close Form</a>\n" +
                "    </form>\n" +
                "    <script src = \"drop.js\"></script>\n" +
                "    </html>";
    }


    public String addPart(Request request, Response response){
        if(request.session().attribute("uName") == null) response.redirect("/Home");
        response.type("text/html");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Welcome to BSTpc!</title>\n" +
                "        <link href=\"https://fonts.googleapis.com/css2?\n" +
                "                    family=Courier+Prime:ital,wght@0,\n" +
                "                    400;0,700;1,400;1,700&display=swap\"\n" +
                "                    rel=\"stylesheet\">\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "    </head>\n" +
                "        <div class=\"logo-reg\">\n" +
                "            <img src=\"logo.gif\" alt=\"TPC\">\n" +
                "            </div>\n" +
                "<form id=\"form\" action=\"/ValidatePart\" method=\"get\">\n" +
                "<p>\n" +
                "    <label for=\"partName\">Part Name: <span id=\"red\">*</span></label>\n" +
                "    <input type=\"text\" name=\"partName\" id=\"partName\" required></input><br/>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <label for=\"price\">Asking Price (USD): <span id=\"red\">*</span></label>\n" +
                "    <input type=\"number\" name=\"price\" id=\"price\" required></input><br/>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <label for=\"description\">Description: <span id=\"red\">*</span></label>\n" +
                "    <input type=\"text\" name=\"description\" id=\"description\" required></input><br/>\n" +
                "</p>\n" +
                "<hr/>\n" +
                "<p>\n" +
                "<label for=\"condition\">Part Condition: </label>\n" +
                "    <select name=\"condition\" id=\"condition\" required>\n" +
                "        <option>Select an Option</option>\n" +
                "        <option value=\"new\">New</option>\n" +
                "        <option value=\"used\">Used</option>\n" +
                "        <option value=\"refurbished\">Refurbished</option>\n" +
                "    </select><br>\n" +
                "</p>\n" +
                "<p>\n" +
                "<label for=\"category\">Category: </label>\n" +
                "    <select name=\"category\" id=\"category\" required>\n" +
                "        <option>Select an Option</option>\n" +
                "        <option value=\"CPU\">CPUs/Processors</option>\n" +
                "        <option value=\"Memory\">Memory</option>\n" +
                "        <option value=\"Motherboards\">Motherboards</option>\n" +
                "        <option value=\"VideoCards\">Video Cards</option>\n" +
                "        <option value=\"Cases\">Computer Cases</option>\n" +
                "        <option value=\"Power\">Power Supplies</option>\n" +
                "        <option value=\"Fans\">Fans & Cooling</option>\n" +
                "        <option value=\"Sound\">Sound Cards</option>\n" +
                "        <option value=\"HDD\">Hard Drives</option>\n" +
                "        <option value=\"SSDs\">SSDs</option>\n" +
                "        <option value=\"Storage\">USB Flash Drives & Memory Cards</option>\n" +
                "    </select>\n" +
                "</p>\n" +
                "<p><input id=\"register\" type=\"submit\" value=\"Add Part\"></input></p><br>\n" +
                "    <a href = \"Home\">Close Form</a>\n" +
                "    </form>\n" +
                "    <script src = \"drop.js\"></script>\n" +
                "    </html>";
    }

    public String register(Request request, Response response){
        if(request.session().attribute("uName") != null) response.redirect("/home");
        response.type("text/html");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Welcome to BSTpc!</title>\n" +
                "        <link href=\"https://fonts.googleapis.com/css2?\n" +
                "                    family=Courier+Prime:ital,wght@0,\n" +
                "                    400;0,700;1,400;1,700&display=swap\"\n" +
                "                    rel=\"stylesheet\">\n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "    </head>\n" +
                "    <div class=\"logo-reg\">\n" +
                "            <img src=\"logo.gif\" alt=\"TPC\">\n" +
                "            </div>\n" +
                "<form id=\"form\" action=\"/AccountCreation\" method=\"get\">\n" +
                "    <p>\n" +
                "        <label for=\"username\">Username: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"text\" name=\"username\" id=\"username\" required></input><br>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "    <p>\n" +
                "        <label for=\"password\">Password: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"password\" name=\"password\" id=\"password\" onchange=\"check()\" requried></input><br>\n" +
                "        </p>\n" +
                "    <p>\n" +
                "        <label for=\"password\">Confirm Password: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"password\" name=\"confirm_password\" id=\"confirm_password\" onchange=\"check()\" requried></input><br>\n" +
                "        <span id = \"message\"></span>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "    <p>\n" +
                "        <label for=\"fName\">First Name: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"text\" name=\"fName\" id=\"fName\" required></input>\n" +
                "        </p>\n" +
                "    <p>\n" +
                "        <label for=\"lName\">Last Name: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"text\" name=\"lName\" id=\"lName\" required></input>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "    <p>\n" +
                "        <label for=\"email\">Email: <span id=\"red\">*</span></label>\n" +
                "        <input type=\"email\" name=\"email\" id=\"email\" required></input>\n" +
                "        </p>\n" +
                "        <br>\n" +
                "        <p>\n" +
                "    <input id=\"register\" id=\"login-button-positive\" type=\"submit\" value=\"Register!\" disabled></input> \n" +
                "        </p>\n" +
                "    <p>\n" +
                "    <br/>\n" +
                "    <!--<button id=\"login-button-negative\" onclick=\"showForm(5)\">Close Form</button>-->\n" +
                "    <a href = \"home\"> Close Form</a>\n" +
                "</form>\n" +
                "<script src = \"drop.js\"></script>\n" +
                "</html>";
    }

    public String logout(Request request, Response response){
        request.session().attribute("uName", null);
        response.redirect("/Home");
        return "";
    }

    public String email(Request request, Response response){
        String from = "tpcbottyboi@gmail.com";
        String pass ="botbot69";
        String to = "andyboyce30@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("This is the Subject Line!");
            message.setText("This is actual message");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
            return "failed";
        }
        return "We made it!";
    }

}
