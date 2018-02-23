package pojo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Cheque {
    @BsonId
    private ObjectId id;
    @BsonProperty("home_id")
    private String homeId;
    private String type;
    private long date;
    private long from;
    private long to;
    private long amount;

    public Cheque() {
    }

    public Cheque(String homeId, String type, long date, long from, long to, long amount) {
        this.homeId = homeId;
        this.type = type;
        this.date = date;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "id='" + id + '\'' +
                ", homeId='" + homeId + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                '}';
    }
}
