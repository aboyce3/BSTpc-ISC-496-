import static spark.Spark.*;


public class SparkEndpoints {
    public static void main(String[] args) {
        EndpointsFunctionality ep = new EndpointsFunctionality();
        staticFileLocation("/public");
        get("/AccountCreation", ep::accountCreation);
        get("/ValidateLogin", ep::loginVerification);
        get("/Home", ep::home);
        get("/Login", ep::login);
        get("/Register", ep::register);
        get("/add_part", ep::addPart);
        get("/home", ep::home);
        get("/login", ep::login);
        get("/test", ep::test);
        get("/register", ep::register);
    }
}