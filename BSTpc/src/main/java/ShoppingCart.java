import java.util.ArrayList;

public class ShoppingCart {
    private String userName;
    private ArrayList<Part> parts;

    public ShoppingCart(String userName){
        this.userName = userName;
        this.parts = new ArrayList<>();
    }

    String getUserName(){return userName;}
    ArrayList<Part> getParts(){return parts;}

    void setUserName(String userName){this.userName = userName;}
    void setPartsList(ArrayList<Part> parts){this.parts = parts;}

    void add(Part part){parts.add(part);}
}