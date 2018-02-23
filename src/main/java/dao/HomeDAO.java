package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import pojo.Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeDAO {
    private final MongoCollection<Home> homeCollection;

    public HomeDAO(MongoDatabase database) {
        homeCollection = database.getCollection("home", Home.class);
        homeCollection.createIndex(Document.parse("{owner: 1}"));
    }

    /**
     *
     * @param owner {@link pojo.User} username
     * @return Unmodifiable {@code List} of {@link pojo.Home} objects
     */
    public List<Home> findOwnerHomes(String owner) {
        List<Home> homes = new ArrayList<>();
        return Collections.unmodifiableList(homeCollection.find(Filters.eq("owner", owner)).into(homes));
    }

    /**
     *
     * @param name {@link pojo.Home} name
     * @return {@code Home} or {@code null}
     */
    public Home findHomeByName(String name) {
        return homeCollection.find(Filters.eq("name", name)).first();
    }
}
