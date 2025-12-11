package mainpkg.ecoresort.Housekeeping;

import mainpkg.ecoresort.User;
import java.util.Random;

public class HousekeepingStaff extends User {

    public HousekeepingStaff() {
    }

    public HousekeepingStaff(String id, String name, String password) {
        super(id, name, password, "housekeeping");
    }

    @Override
    public String generateID() {
        Random r = new Random();
        int id = r.nextInt(9000) + 1000;
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
