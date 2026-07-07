package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static MockWebServer mockServer;
    private Javalin app;

    @BeforeAll
    static void setUpServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void tearDown() {
        mockServer.close();
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        System.setProperty(
                "JDBC_DATABASE_URL",
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        );
        app = App.getApp();
        UrlRepository.removeAll();
        UrlCheckRepository.removeAll();
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
        var urlStr = "https://www.example.com";
        UrlRepository.save(new Url(urlStr));

        var savedUrl = UrlRepository.findByName(urlStr);
        assertThat(savedUrl).isPresent();

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(savedUrl.get().getId()));

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string())
                    .contains(urlStr);
        });
    }

    @Test
    public void testUrlNoExistPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("999"));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCheck() throws SQLException, IOException {
        mockServer.enqueue(new MockResponse.Builder()
                .body("""
                        <html>
                            <head>
                                <title>Test page</title>
                                <meta name="description" content="Test description">
                            </head>
                            <body>
                                <h1>Hello world</h1>
                            </body>
                        </html>
                        """)
                .build());

        mockServer.start();
        var urlStr = mockServer.url("/").toString();

        var savedUrl = new Url(urlStr);
        UrlRepository.save(savedUrl);

        var urlFromDb = UrlRepository.findByName(urlStr)
                .orElseThrow();

        JavalinTest.test(app, (serverApp, client) -> {

            var response = client.post(
                    NamedRoutes.urlCheckPath(urlFromDb.getId())
            );

            assertThat(response.code()).isEqualTo(302);
        });

        var checks = UrlCheckRepository.findAllByUrlId(urlFromDb.getId());
        assertThat(checks).hasSize(1);

        var check = checks.getFirst();

        assertThat(check.getStatusCode()).isEqualTo(200);
        assertThat(check.getTitle()).isEqualTo("Test page");
        assertThat(check.getH1()).isEqualTo("Hello world");
        assertThat(check.getDescription()).isEqualTo("Test description");
        assertThat(check.getUrlId()).isEqualTo(urlFromDb.getId());
    }
}
