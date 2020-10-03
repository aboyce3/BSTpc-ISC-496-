public class WishList {
    String userName;
    Part part;

    public WishList(String userName, Part part){
        this.userName = userName;
        this.part = part;
    }

    String getUserName(){return userName;}
    Part getPart(){return part;}

    void setUserName(String userName){this.userName = userName;}
    void setPart(Part part){this.part = part;}
}
