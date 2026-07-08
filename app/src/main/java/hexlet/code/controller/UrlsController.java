package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Flash;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class UrlsController {
    public static void base(Context ctx) {
        var page = new BasePage();
        Flash.bind(ctx, page);
        ctx.render("index.jte", Map.of("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = Optional.ofNullable(ctx.formParam("url")).orElse("");

        String name;
        try {
            var parsedUrl = new URI(inputUrl).toURL();
            var protocol = parsedUrl.getProtocol();
            var host = parsedUrl.getHost();
            var port = parsedUrl.getPort();
            name = port == -1
                    ? String.format("%s://%s", protocol, host)
                    : String.format("%s://%s:%d", protocol, host, port);
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            Flash.danger(ctx, "Некорректный URL");
            ctx.status(422);
            base(ctx);
            return;
        }

        if (name.length() > 255) {
            Flash.danger(ctx, "URL превышает 255 символов");
            ctx.status(422);
            base(ctx);
            return;
        }

        var existingUrl = UrlRepository.findByName(name);
        if (existingUrl.isPresent()) {
            Flash.danger(ctx, "Страница уже существует");
            ctx.redirect(NamedRoutes.urlPath(existingUrl.get().getId()));
            return;
        }

        var url = new Url(name);
        UrlRepository.save(url);
        Flash.success(ctx, "Страница успешно добавлена");
        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntitiesWithInfo();
        var page = new UrlsPage(urls);
        Flash.bind(ctx, page);
        ctx.render("urls/index.jte", Map.of("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var checks = UrlCheckRepository.findAllByUrlId(url.getId());
        var page = new UrlPage(url, checks);
        Flash.bind(ctx, page);
        ctx.render("urls/show.jte", Map.of("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlRepository.find(id)
                .orElseThrow(() ->
                        new NotFoundResponse("Entity with id = " + id + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName())
                    .asString();

            int statusCode = response.getStatus();

            if (statusCode >= 400) {
                throw new IOException("Bad status code: " + statusCode);
            }

            Document document = Jsoup.parse(response.getBody());

            String title = document.title();

            String h1 = document.selectFirst("h1") != null
                    ? Objects.requireNonNull(document.selectFirst("h1")).text()
                    : null;

            String description = document.selectFirst("meta[name=description]") != null
                    ? Objects.requireNonNull(document.selectFirst("meta[name=description]")).attr("content")
                    : null;

            UrlCheck check = new UrlCheck(
                    statusCode,
                    title,
                    h1,
                    description,
                    id
            );

            UrlCheckRepository.save(check);
            Flash.success(ctx, "Страница успешно проверена");
        } catch (Exception e) {
            e.printStackTrace();
            Flash.danger(ctx, "Произошла ошибка при проверке");
        } finally {
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
