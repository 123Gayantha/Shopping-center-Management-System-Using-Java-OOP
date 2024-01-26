package westminster_shopping_center.user;

public class WestminsterShoppingManager extends BaseUser {
    public WestminsterShoppingManager(String username, String password){
        super(username, password);
    }

    public static WestminsterShoppingManager signIn(String username, String password) {
        for (BaseUser user : BASE_USERS){
            if (user instanceof WestminsterShoppingManager && user.getUsername().equals(username)){
                user.authenticate(password);
                return (WestminsterShoppingManager) user;
            }
        }

        throw new IllegalArgumentException("Username does not exist");
    }
}
