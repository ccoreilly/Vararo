package cat.oreilly.vararo.model;

import com.orm.SugarRecord;

import java.util.List;
import java.util.UUID;

/**
 * Created by ciaran on 20/12/17.
 */

public class InventoryItem extends SugarRecord {
    String name;
    String mainPicture;
    List<String> pictures;
    UUID id;
    UUID parent;
    List<InventoryTag> tags;

    public InventoryItem(){}

    public InventoryItem(String name, String mainPicture, List<String> pictures, UUID parent, List<InventoryTag> tags) {
        this.name = name;
        this.mainPicture = mainPicture;
        this.pictures = pictures;
        this.parent = parent;
        this.tags = tags;
        this.id = UUID.randomUUID();
    }
    public String getName() {
        return name;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public UUID getUid() {
        return id;
    }

    public UUID getParent() {
        return parent;
    }

    public List<InventoryTag> getTags() {
        return tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public void setTags(List<InventoryTag> tags) {
        this.tags = tags;
    }

}
