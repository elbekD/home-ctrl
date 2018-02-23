package dao;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pojo.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Created by Elbek D. on 01.11.2017.
 */
public final class UserDAO {
    private final MongoCollection<User> usersCollection;
    private final Random random = new SecureRandom();

    public UserDAO(final MongoDatabase database) {
        usersCollection = database.getCollection("users", User.class);
        usersCollection.createIndex(Document.parse("{email: 1}, {unique: true}"));
    }

    public boolean addUser(User user) {
        String passwordHash = makePasswordHash(user.getPassword(), Integer.toString(random.nextInt()));
        user.setPassword(passwordHash);
        try {
            usersCollection.insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Username already in use: " + user.getUsername());
                return false;
            }
            throw e;
        }
    }

    public User findByUsername(String username) {
        return usersCollection.find(new Document("_id", username)).first();
    }

    public boolean hasUsername(String username) {
        User u = usersCollection.find(new Document("_id", username)).first();
        return u != null;
    }

    public boolean hasEmail(String email) {
        User u = usersCollection.find(new Document("email", email)).first();
        return u != null;
    }

    public User validateLogin(String username, String password) {
        User user = usersCollection.find(new Document("_id", username)).first();

        if (user == null) {
            System.out.println("User not in database");
            return null;
        }

        String hashedAndSalted = user.getPassword();

        String salt = hashedAndSalted.split(",")[1];

        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
            System.out.println("Submitted password does not match");
            return null;
        }

        return user;
    }

    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encodeToString(hashedBytes) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }
}
