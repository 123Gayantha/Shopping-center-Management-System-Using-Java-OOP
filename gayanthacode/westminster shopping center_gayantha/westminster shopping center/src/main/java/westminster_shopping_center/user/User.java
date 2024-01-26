package westminster_shopping_center.user;

import westminster_shopping_center.ShoppingCart;

public class User extends BaseUser {
    public ShoppingCart shoppingCart = new ShoppingCart();

    public User(String username, String password){
        super(username, password);
    }

    public static User signIn(String username, String password) {
        for (BaseUser user : BASE_USERS){
            if (user instanceof User && user.getUsername().equals(username)){
                user.authenticate(password);
                return (User) user;
            }
        }

        throw new IllegalArgumentException("Username does not exist");
    }
}
