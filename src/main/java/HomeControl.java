import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import dao.ChequeDAO;
import dao.HomeDAO;
import dao.SessionDAO;
import dao.UserDAO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import pojo.Cheque;
import pojo.Home;
import pojo.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utils.NumberToDateFormatFactory;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class HomeControl {
    private final static String DATABASE_NAME = "home_ctrl";
    private final static String DEFAULT_DATABASE_URL = "mongodb://localhost";

    private MongoDatabase database;
    private Configuration cfg;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;
    private ChequeDAO chequeDAO;
    private HomeDAO homeDAO;

    private HomeControl(final String mongoUrl) throws IOException {
        initMongoDB(mongoUrl);
        initDAOs();
        initConfiguration();
        port(8080);
        initRoutes();
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new HomeControl(DEFAULT_DATABASE_URL);
        } else {
            new HomeControl(args[0]);
        }
    }

    private void initMongoDB(final String mongoUrl) {
        CodecRegistry pojoCodecRegistry
                = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoUrl));
        database = mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
    }

    private void initDAOs() {
        userDAO = new UserDAO(database);
        sessionDAO = new SessionDAO(database);
        chequeDAO = new ChequeDAO(database);
        homeDAO = new HomeDAO(database);
    }

    private void initConfiguration() {
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(HomeControl.class, "/freemarker");
        cfg.setCustomNumberFormats(Map.of("mDate", NumberToDateFormatFactory.INSTANCE));
        staticFiles.location("/static");
    }

    private void initRoutes() throws IOException {

        get("/", new AbstractRoute("index.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                String username = sessionDAO.findUsernameBySessionId(getSessionCookie(request));
                if (username == null) {
                    response.redirect("/login");
                } else {
                    Map<String, Object> map = new HashMap<>();

                    User user = userDAO.findByUsername(username);
                    map.put("username", user.getFirstname());

                    List<Home> homes = homeDAO.findOwnerHomes(username);
                    if (!homes.isEmpty()) {
                        Home home = homes.get(0);
                        List<Cheque> cheques = chequeDAO.findChequeByHomeId(home.getId().toHexString());
                        map.put("cards", cheques);
                    }

                    template.process(map, writer);
                }
            }
        });

        get("/login", new AbstractRoute("login.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                Map<String, String> map = Map.of("username", "", "login_error", "");
                template.process(map, writer);
            }
        });

        post("/login", new AbstractRoute("login.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                User user = userDAO.validateLogin(username, password);
                if (user != null) {
                    String sessionId = sessionDAO.startSession(username);
                    response.raw().addCookie(new Cookie("session", sessionId));
                    response.redirect("/");
                } else {
                    Map<String, String> map = Map.of("username", username, "login_error", "Login or password is incorrect");
                    template.process(map, writer);
                }
            }
        });

        get("/logout", new AbstractRoute("index.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                String sessionId = getSessionCookie(request);
                if (sessionId != null) {
                    sessionDAO.endSession(sessionId);
                    Cookie c = getSessionCookieActual(request);
                    c.setMaxAge(0);
                    response.raw().addCookie(c);
                }
                response.redirect("/login");
            }
        });

        get("/signup", new AbstractRoute("signup.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                Map<String, String> map = new HashMap<>();
                map.put("username", "");
                map.put("firstname", "");
                map.put("lastname", "");
                map.put("email", "");
                map.put("username_error", "");
                map.put("email_error", "");
                template.process(map, writer);
            }
        });

        post("/signup", new AbstractRoute("signup.ftl") {
            @Override
            void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException {
                String username = request.queryParams("login");
                String password = request.queryParams("password");
                String firstname = request.queryParams("firstname");
                String lastname = request.queryParams("lastname");
                String email = request.queryParams("email");

                User user = new User(username, password, firstname, lastname, email);
                Map<String, String> map = new HashMap<>();

                if (validateSignup(user, map)) {
                    if (!userDAO.addUser(user)) {
                        response.redirect("/internal_error");
                        return;
                    }

                    String sessionId = sessionDAO.startSession(user.getUsername());
                    response.raw().addCookie(new Cookie("session", sessionId));
                    response.redirect("/");
                } else {
                    map.put("username", username);
                    map.put("firstname", firstname);
                    map.put("lastname", lastname);
                    map.put("email", email);
                    template.process(map, writer);
                }
            }
        });
    }

    private boolean validateSignup(User user, Map<String, String> map) {
        if (userDAO.hasUsername(user.getUsername())) {
            map.put("username_error", "Username already exists");
            return false;
        }

        if (userDAO.hasEmail(user.getEmail())) {
            map.put("email_error", "Email already exists");
            return false;
        }
        return true;
    }

    private String getSessionCookie(final Request request) {
        if (request.raw().getCookies() == null)
            return null;

        Cookie cookie = Arrays.stream(request.raw().getCookies()).filter(c -> c.getName().equals("session")).findFirst().orElse(null);
        return cookie != null ? cookie.getValue() : null;
    }

    private Cookie getSessionCookieActual(final Request request) {
        if (request.raw().getCookies() == null)
            return null;

        Cookie cookie = Arrays.stream(request.raw().getCookies()).filter(c -> c.getName().equals("session")).findFirst().orElse(null);
        return cookie;
    }

    private abstract class AbstractRoute implements Route {
        final Template template;

        AbstractRoute(String templateName) throws IOException {
            template = cfg.getTemplate(templateName);
        }

        @Override
        public Object handle(Request request, Response response) throws Exception {
            StringWriter writer = new StringWriter();
            try {
                doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        abstract void doHandle(Request request, Response response, StringWriter writer) throws IOException, TemplateException;
    }
}
