import static spark.Spark.*;

public class SparkEndpoints {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}