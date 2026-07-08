package hexlet.code.repository;

import hexlet.code.dto.UrlInfo;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            var createdAt = LocalDateTime.now();
            preparedStatement.setTimestamp(2, Timestamp.valueOf(createdAt));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var car = new Url(name);
                car.setId(id);
                car.setCreatedAt(createdAt);
                return Optional.of(car);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<UrlInfo> getEntitiesWithInfo() throws SQLException {
        var sql = """
                SELECT
                    urls.id,
                    urls.name,
                    urls.created_at,
                
                    url_checks.id AS check_id,
                    url_checks.status_code,
                    url_checks.title,
                    url_checks.h1,
                    url_checks.description,
                    url_checks.url_id,
                    url_checks.created_at AS check_created_at
                
                FROM urls
                
                LEFT JOIN url_checks
                ON urls.id = url_checks.url_id
                AND url_checks.id = (
                    SELECT MAX(id)
                    FROM url_checks
                    WHERE url_id = urls.id
                )
                
                ORDER BY urls.id
                """;

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var resultSet = stmt.executeQuery()) {

            var urls = new ArrayList<UrlInfo>();

            while (resultSet.next()) {
                var url = new Url(
                        resultSet.getString("name")
                );

                url.setId(resultSet.getLong("id"));
                url.setCreatedAt(
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );

                UrlCheck lastCheck = null;

                var checkId = resultSet.getObject("check_id");

                if (checkId != null) {
                    lastCheck = new UrlCheck(
                            ((Number) checkId).longValue(),
                            resultSet.getInt("status_code"),
                            resultSet.getString("title"),
                            resultSet.getString("h1"),
                            resultSet.getString("description"),
                            resultSet.getLong("url_id"),
                            resultSet.getTimestamp("check_created_at")
                                    .toLocalDateTime()
                    );
                }

                urls.add(new UrlInfo(url, lastCheck));
            }

            return urls;
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM urls";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
