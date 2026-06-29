package hexlet.code;

import io.javalin.Javalin;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static Javalin getApp() {
        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.routes.before(ctx -> {
                ctx.contentType("text/html; charset=utf-8");
            });
            config.routes.get("/", ctx -> ctx.result("Hello World"));
        });
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }
}
