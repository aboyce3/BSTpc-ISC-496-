import spark.Request;
import spark.Response;

import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EndpointsFunctionality {

    public String accountCreation(Request request, Response response){
        String userName = request.queryParams("userName");
        String password = request.queryParams("password");
        String password2 = request.queryParams("password2");
        String email = request.queryParams("email");
        String first = request.queryParams("first");
        String last = request.queryParams("last");
        Connection con;
        String insert = "INSERT into users " + "(username, password, email, first, last)" + "VALUES " + "('" + userName + "','"
                + password + "','" + email + "','" + first + "','" + last + "')";
        String lookup = "SELECT * FROM TPC_DB.users WHERE username = '" + userName + "'";
        Statement statement = null;
        ResultSet result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu:3306/TPC_DB", "aboyce3", "mysql");
            statement = con.createStatement();
            result = statement.executeQuery(lookup);
            while (result.next()) {
                String uName = result.getString("username");
                if (userName.contentEquals(uName))
                    response.redirect("/ErrorCreating");
                return "";
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        response.type("text/html");
        if (userName == null || "".equals(userName) || password == null || "".equals(password) || password2 == null
                || "".equals(password2)) {
            response.redirect("/ErrorCreating");
        } else if (!Pattern.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?"
                + "^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
                + "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\"
                + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", email)) {
            response.redirect("/ErrorCreating");
            return "";
        } else if (password == null || "".equals(password) || !password.equals(password2)) {
            response.redirect("/ErrorCreating");
        } else {
            try {
                if (statement != null) {
                    statement.executeUpdate(insert);
                    request.session().attribute("username", userName);
                    request.session().maxInactiveInterval(9999);
                    response.redirect("/Home");
                    return"";
                } else
                    response.redirect("/ErrorCreating");
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.redirect("/ErrorCreating");
        return "";
    }

    public String loginVerification(Request request, Response response){
        String userName = request.queryParams("userName");
        String password = request.queryParams("password");
        Connection con = null;
        String lookup = "SELECT * FROM TPC_DB.users WHERE username = '" + userName + "' AND password = '"
                + password + "';";
        Statement statement = null;
        ResultSet results = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu:3306/TCP_DB", "aboyce3", "mysql");
            statement = con.createStatement();
            results = statement.executeQuery(lookup);
            while (results.next()) {
                String uName = results.getString("username");
                String pass = results.getString("password");
                if (userName.contentEquals(uName) && password.contentEquals(pass)) {
                    request.session().attribute("username", userName);
                    request.session().maxInactiveInterval(99999);;
                    response.redirect("/Home");
                    return "";
                }
            }
            response.redirect("/ErrorLogin");
            return "";
        } catch (SQLException | ClassNotFoundException e) {
            response.redirect("ErrorLogin");
            return "";
        }
    }

    public String home(Request request, Response response){
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
                "    <body>\n" +
                "        <div class=\"header\">\n" +
                "            <a href=\"#home\"># Home</a>\n" +
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
                "            </div>\n" +
                "            <a id=\"center\" href=\"add_part\">Add Part</a>\n" +
                "            <a id=\"right\" href=\"login\">Login</a>\n" +
                "            <a id=\"right\" href=\"register\">Register</a>\n" +
                "        <div class=\"logo\">\n" +
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
                "<form id=\"loginForm\">\n" +
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
                "<form id=\"loginForm\">\n" +
                "<p>\n" +
                "    <label for=\"partName\">Part Name: <span id=\"red\">*</span></label>\n" +
                "    <input type=\"text\" name=\"partName\" id=\"partName\" required></input><br/>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <label for=\"price\">Asking Price (USD): <span id=\"red\">*</span></label>\n" +
                "    <input type=\"number\" name=\"price\" id=\"price\" required></input><br/>\n" +
                "</p>\n" +
                "<!--\n" +
                "<p>\n" +
                "    <label for=\"serial\">Serial #</label>\n" +
                "    <input type=\"number\" name=\"serial\" id=\"serial\"></input><br/>\n" +
                "</p>\n" +
                "-->\n" +
                "<p>\n" +
                "    <label for=\"description\">Description <span id=\"red\">*</span></label>\n" +
                "    <input type=\"text\" name=\"description\" id=\"description\" required></input><br/>\n" +
                "</p>\n" +
                "<hr/>\n" +
                "<h2 id=\"flair\">Condition</h2>\n" +
                "<div id=\"checks\">\n" +
                "<p>\n" +
                "    <input type=\"checkbox\" id=\"new\" name=\"new\" value=\"new\">\n" +
                "    <label for=\"new\">New</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <input type=\"checkbox\" id=\"used\" name=\"used\" value=\"used\">\n" +
                "    <label for=\"used\">Used</label>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <input type=\"checkbox\" id=\"refurbished\" name=\"refurbished\" value=\"refurbished\">\n" +
                "    <label for=\"refurbished\">Refurbished</label>\n" +
                "</p>\n" +
                "</div><br>\n" +
                "<p><input id=\"register\" type=\"submit\" value=\"Add Part\"></input></p><br>\n" +
                "    <a href = \"home\"> Close Form</a>\n" +
                "    </form>\n" +
                "    <script src = \"drop.js\"></script>\n" +
                "    </html>";
    }

    public String register(Request request, Response response){
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
                "<form id=\"regForm\">\n" +
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

    public String test(Request request, Response response){
        Connection con = null;
        String lookup = "SELECT * FROM TPC_DB.users;";
        Statement statement = null;
        ResultSet results = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://pi.cs.oswego.edu:3306/TCP_DB", "aboyce3", "mysql");
            statement = con.createStatement();
            results = statement.executeQuery(lookup);
            return "You're Connected!";
        } catch (SQLException | ClassNotFoundException e) {
            return "Not Connected";
        }
    }

}
