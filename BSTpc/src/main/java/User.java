import java.util.ArrayList;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;

    public User(String user, String First, String Last, String email){
        firstName = First;
        lastName = Last;
        this.email = email;
        userName = user;
    }

    String getFirstName(){return firstName;}
    String getLastName(){return lastName;}
    String getEmail(){return email;}
    String getUserName(){return userName;}

    void setFirstName(String First){firstName = First;}
    void setLastName(String Last){lastName = Last;}
    void setEmail(String email){this.email = email;}
    void setUserName(String userName){this.userName = userName;}
}
