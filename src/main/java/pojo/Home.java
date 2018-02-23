package pojo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Home {
    @BsonId
    private ObjectId id;
    private String owner;
    private String name;

    public Home() {
    }

    /**
     * Home constructor
     *
     * @param id    BsonId
     * @param owner {@link pojo.User} username
     * @param name  name
     */
    public Home(String owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Home{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
