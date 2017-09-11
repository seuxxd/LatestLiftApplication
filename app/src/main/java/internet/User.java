package internet;

/**
 * Created by SEUXXD on 2017-09-11.
 */

public class User {
    public String userName;
    public String passWord;

    public User(String username, String password) {
        userName = username;
        passWord = password;
    }

    public String getPassword() {
        return passWord;
    }

    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        passWord = password;
    }

    public void setUsername(String username) {
        userName = username;
    }
}
