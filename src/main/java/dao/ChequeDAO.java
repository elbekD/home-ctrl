package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import pojo.Cheque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChequeDAO {
    private final MongoCollection<Cheque> chequeCollection;

    public ChequeDAO(final MongoDatabase database) {
        chequeCollection = database.getCollection("cheques", Cheque.class);
        chequeCollection.createIndex(Document.parse("{home_id: 1, date: 1}"));
    }

    /**
     *
     * @param homeId {@link pojo.Home} id
     * @return Unmodifiable list of {@link pojo.Cheque}
     */
    public List<Cheque> findChequeByHomeId(String homeId) {
        List<Cheque> list = new ArrayList<>();
        return Collections.unmodifiableList(chequeCollection.find(Filters.eq("home_id", homeId)).into(list));
    }

    /**
     *
     * @param type one of {@link pojo.Type} or user defined type
     * @return Unmodifiable list of {@link pojo.Cheque}
     */
    public List<Cheque> findChequeByType(String type) {
        List<Cheque> list = new ArrayList<>();
        return Collections.unmodifiableList(chequeCollection.find(Filters.eq("type", type)).into(list));
    }
}
