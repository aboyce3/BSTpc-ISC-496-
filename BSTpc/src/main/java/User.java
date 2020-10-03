import java.util.ArrayList;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String img_Location;
    private ShoppingCart cart;
    private Auctioned userAuctionedItems;
    private WishList wishlist;

    public User(String user, String First, String Last, String email, String img){
        firstName = First;
        lastName = Last;
        this.email = email;
        img_Location = img;
        userName = user;
        cart = new ShoppingCart(user);
        userAuctionedItems = new Auctioned(user);
        wishlist = new WishList(user);
    }

    String getFirstName(){return firstName;}
    String getLastName(){return lastName;}
    String getEmail(){return email;}
    String getUserName(){return userName;}
    String getIMG(){return img_Location;}

    void setFirstName(String First){firstName = First;}
    void setLastName(String Last){lastName = Last;}
    void setEmail(String email){this.email = email;}
    void setIMG(String IMG){img_Location = IMG;}
    void setUserName(String userName){this.userName = userName;}
    void setWishList(WishList wishlist) {this.wishlist = wishlist;}
    void setAuctionedList(Auctioned auctioned) {this.userAuctionedItems = auctioned;}
    void setShoppingCart(ShoppingCart cart) {this.cart = cart;}

    void addToCart(Part item){cart.add(item);}
    void addToAuctionedList(Part item){userAuctionedItems.add(item);}
    void addToWishlist(Part item){wishlist.add(item);}
}
