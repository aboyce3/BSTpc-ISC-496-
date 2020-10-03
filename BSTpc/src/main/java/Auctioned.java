public class Auctioned {
    String userName;
    Part part;

    public Auctioned(String userName, Part part){
        this.userName = userName;
        this.part = part;
    }

    String getUserName(){return userName;}
    Part getPart(){return part;}

    void setUserName(String userName){this.userName = userName;}
    void setPart(Part part){this.part = part;}
}
