package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        System.setProperty(
                "JDBC_DATABASE_URL",
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        );
        app = App.getApp();
        UrlRepository.removeAll();
    }

    @Test
    public void testBasePage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.basePath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://www.example.com";
            var requestBody = "url=" + url;
            var response = client.post(NamedRoutes.basePath(), requestBody);
            assertThat(response.code()).isEqualTo(302);
            var saved = UrlRepository.findByName(url);
            assertThat(saved).isPresent();
            assertThat(saved.get().getName()).isEqualTo(url);
        });
    }

    @Test
    public void testCreateUrlAlreadyExists() {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://www.example.com";
            var urlEntity = new Url(url);
            UrlRepository.save(urlEntity);
            var requestBody = "url=" + url;
            var response = client.post(NamedRoutes.basePath(), requestBody);
            assertThat(response.code()).isEqualTo(302);
            var saved = UrlRepository.findByName(url);
            assertThat(saved).isPresent();
        });
    }

    @Test
    public void testCreateUrlInvalid() {
        JavalinTest.test(app, (server, client) -> {
            var invalidUrl = "not-a-valid-url";
            var requestBody = "url=" + invalidUrl;
            var response = client.post(NamedRoutes.basePath(), requestBody);
            assertThat(response.code()).isEqualTo(422);
            var saved = UrlRepository.findByName(invalidUrl);
            assertThat(saved).isNotPresent();
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://www.example.com");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }
}
