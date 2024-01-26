package westminster_shopping_center.user;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseUser implements Serializable {
    protected static final List<BaseUser> BASE_USERS = new ArrayList<>();
    // Username is Unique
    private String username;
    private String password;

    public BaseUser(String username, String password){
        for (BaseUser user : BASE_USERS){
            if (user.getUsername().equals(username)){
                throw new IllegalArgumentException("Username already exists");
            }
        }
        this.username = username;
        this.password = hashStringWithMD5(password);
    }

    public void save(){
        BASE_USERS.add(this);
    }

    public static BaseUser signIn(String username, String password){
        for (BaseUser user : BASE_USERS){
            if (user.getUsername().equals(username)){
                user.authenticate(password);
                return user;
            }
        }
        throw new IllegalArgumentException("Username does not exist");
    }

    public String getUsername() {
        return username;
    }

    public void authenticate(String password){
        if (!this.password.equals(hashStringWithMD5(password))){
            throw new IllegalArgumentException("Wrong password");
        }
    }

    public void changePassword(String oldPassword, String newPassword){
        authenticate(oldPassword);
        this.password = hashStringWithMD5(newPassword);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void dump(ObjectOutputStream oos) throws Exception {
        oos.writeObject(BASE_USERS);
    }

    @SuppressWarnings("unchecked")
    public static void load(ObjectInputStream ois) throws Exception {
        List<BaseUser> users = (List<BaseUser>) ois.readObject();
        BASE_USERS.clear();
        BASE_USERS.addAll(users);
    }

    private static String hashStringWithMD5(String input) {
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());

        byte[] byteData = md.digest();

        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteData) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            return input;
        }
    }
}
