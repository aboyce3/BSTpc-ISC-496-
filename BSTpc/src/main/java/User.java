public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String img_Location;

    public User(String User, String First, String Last, String email, String img){
        firstName = First;
        lastName = Last;
        this.email = email;
        img_Location = img;
        userName = User;
    }

    String getfirstName(){ return firstName;}
    String getlastName(){ return lastName;}
    String getemail(){ return email;}
    String getuserName(){ return userName;}
    String getIMG(){ return img_Location;}

    void setfirstName(String First){firstName = First;}
    void setlastName(String Last){lastName = Last;}
    void setemail(String email){this.email = email;}
    void setIMG(String IMG){img_Location = IMG;}
    void setuserName(String userName){this.userName = userName;}
}
