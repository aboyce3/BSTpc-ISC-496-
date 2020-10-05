import static spark.Spark.*;

public class SparkEndpoints {
    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/hello", (req, res) -> "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Welcome to BSTpc!</title>\n" +
                "        \n" +
                "        <link href=\"https://fonts.googleapis.com/css2?\n" +
                "                    family=Courier+Prime:wght@400;700&\n" +
                "                    family=Oswald:wght@300;400&\n" +
                "                    family=Secular+One&display=swap\"\n" +
                "                    rel=\"stylesheet\"> \n" +
                "        <link rel=\"stylesheet\" type=\"text/css\" href=\"http://localhost:4567/CSS/styles.css\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"header\">\n" +
                "            <a href=\"#\">Home</a>\n" +
                "            <a href=\"contact.html\">Contact Us</a>\n" +
                "            <a href=\"help.html\">Help</a>\n" +
                "            <a href=\"store.html\">Store</a>\n" +
                "                <div class=\"search\">\n" +
                "                        <input type=\"text\" placeholder=\"Search...\">\n" +
                "                        <button class=\"search-button\">Submit</button>\n" +
                "            </div>\n" +
                "            <div class=\"second-header\">\n" +
                "            <a href=\"account.html\">My Account</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <!-- Dropdown courtesy of w3Schools:\n" +
                "        https://www.w3schools.com/howto/howto_js_dropdown_sidenav.asp -->\n" +
                "    <div class=\"sidebar\">\n" +
                "        <a href=\"#Builds\">Builds</a>\n" +
                "        <button class=\"sidebar-dropdown\">Peripherals &#9660;</button>\n" +
                "            <div class=\"down\">\n" +
                "                <a href=\"#\">Monitors</a>\n" +
                "                <a href=\"#\">Mice</a>\n" +
                "                <a href=\"#\">Keyboards</a>\n" +
                "            </div>\n" +
                "            <button class=\"sidebar-dropdown\">PC Components &#9660;\n" +
                "                <i class=\"fa fa-caret-down\"></i></button>\n" +
                "                <div class=\"down\">\n" +
                "                    <a href=\"#\">CPUs</a>\n" +
                "                    <a href=\"#\">GPUs</a>\n" +
                "                    <a href=\"#\">RAM</a>\n" +
                "                    <button class=\"sidebar-dropdown\">Hard Drives &#9660;\n" +
                "                        <i class=\"fa fa-caret-down\"></i></button>\n" +
                "                        <div class=\"down\">\n" +
                "                            <a href=\"#\">HDDs</a>\n" +
                "                            <a href=\"#\">SSDs</a>\n" +
                "                        </div>\n" +
                "                </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "        var descend = document.getElementsByClassName(\"sidebar-dropdown\")\n" +
                "        var i;\n" +
                "\n" +
                "        for(i = 0; i < descend.length; i++){\n" +
                "            descend[i].addEventListener(\"click\", function(){\n" +
                "                this.classList.toggle(\"active\");\n" +
                "                var content = this.nextElementSibling;\n" +
                "                if(content.style.display === \"block\"){\n" +
                "                    content.style.display = \"none\";\n" +
                "                } else{\n" +
                "                    content.style.display = \"block\";\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "    </body>\n" +
                "</html>\n");
    }
}