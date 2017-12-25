package cat.oreilly.vararo.model;

import com.orm.SugarRecord;

/**
 * Created by ciaran on 20/12/17.
 */

public class InventoryTag extends SugarRecord {
    String name;

    public InventoryTag(){}

    public InventoryTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
