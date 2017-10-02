package king.zach.pynny.database.models;

import java.io.Serializable;

/**
 * Created by Zach on 9/11/2017.
 */

public class Category implements Serializable {

    private long id;
    private String name;
    private boolean isIncome;

    public Category() {

    }

    public Category(String name, boolean isIncome) {
        this.name = name;
        this.isIncome = isIncome;
    }

    public Category(long id, String name, boolean isIncome) {
        this.id = id;
        this.name = name;
        this.isIncome = isIncome;
    }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIsIncome(boolean isIncome) { this.isIncome = isIncome; }

    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public boolean getIsIncome() { return this.isIncome; }

    public String toString() {
        return this.name;
    }

}
