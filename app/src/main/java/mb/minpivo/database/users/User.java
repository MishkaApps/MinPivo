package mb.minpivo.database.users;

import mb.minpivo.database.DatabaseItem;

/**
 * Created by mbolg on 22.12.2017.
 */
public class User implements DatabaseItem{
    String email;
    String name;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
