import static spark.Spark.*;


public class SparkEndpoints {
    public static void main(String[] args) {
        EndpointsFunctionality ep = new EndpointsFunctionality();
        staticFileLocation("/public");
        get("/AccountCreation", ep::accountCreation);
        get("/ValidateLogin", ep::loginVerification);
        get("/Home", ep::home);
        get("/SearchName", ep::searchByName);
        get("/Login", ep::login);
        get("/Register", ep::register);
        get("/AddPart", ep::addPart);
        get("/Email", ep::email);
        get("/ValidatePart", ep::validatePart);
        get("/Search", ep::search);
        get("/home", ep::home);
        get("/login", ep::login);
        get("/logout", ep::logout);
        get("/register", ep::register);
        get("/", ep::home);
    }
}