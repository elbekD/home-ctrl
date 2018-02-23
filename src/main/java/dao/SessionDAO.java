package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

/**
 * Created by Elbek D. on 01.11.2017.
 */
public final class SessionDAO {
    private final MongoCollection<Document> sessionCollection;

    public SessionDAO(final MongoDatabase database) {
        this.sessionCollection = database.getCollection("sessions");
    }

    public String findUsernameBySessionId(String sessionId) {
        Document session = getSession(sessionId);

        if (session == null)
            return null;

        return session.get("username").toString();
    }

    public String startSession(String username) {
        SecureRandom random = new SecureRandom();
        Base64.Encoder encoder = Base64.getEncoder();

        byte randomBytes[] = new byte[32];
        random.nextBytes(randomBytes);

        String sessionId = encoder.encodeToString(randomBytes);

        Document document = new Document(Map.of("username", username, "_id", sessionId));
        sessionCollection.insertOne(document);

        return sessionId;
    }

    public boolean endSession(String sessionId) {
        return sessionCollection.deleteOne(Filters.eq("_id", sessionId)).wasAcknowledged();
    }

    private Document getSession(String sessionId) {
        return sessionCollection.find(Filters.eq("_id", sessionId)).first();
    }
}
