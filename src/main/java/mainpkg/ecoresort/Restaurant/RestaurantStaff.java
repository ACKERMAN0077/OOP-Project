package mainpkg.ecoresort.Restaurant;

import mainpkg.ecoresort.User;
import java.util.Random;

public class RestaurantStaff extends User {

    public RestaurantStaff() {
    }

    public RestaurantStaff(String id, String name, String password) {
        super(id, name, password, "restaurant");
    }

    @Override
    public String generateID() {
        Random r = new Random();
        int id = r.nextInt(90000) + 10000;
        return String.valueOf(id);
    }

    @Override
    public boolean login(String id, String password) {
        if (this.id.equals(id) && this.password.equals(password)) {
            return true;
        }
        return false;
    }
}
