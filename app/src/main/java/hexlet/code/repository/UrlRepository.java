package hexlet.code.repository;

import hexlet.code.dto.UrlInfo;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    private static Url mapUrl(ResultSet rs) throws SQLException {
        var url = new Url(rs.getString("name"));
        url.setId(rs.getLong("id"));
        url.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return url;
    }

    private static UrlCheck mapUrlCheck(ResultSet rs) throws SQLException {
        var checkId = rs.getLong("check_id");

        if (rs.wasNull()) {
            return null;
        }

        return new UrlCheck(
                checkId,
                rs.getInt("status_code"),
                rs.getString("title"),
                rs.getString("h1"),
                rs.getString("description"),
                rs.getLong("url_id"),
                rs.getTimestamp("check_created_at").toLocalDateTime()
        );
    }

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, url.getName());
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("DB has not returned an id after saving an entity");
                }

                url.setId(generatedKeys.getLong(1));
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (var rs = stmt.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapUrl(rs))
                        : Optional.empty();
            }
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            try (var rs = stmt.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapUrl(rs))
                        : Optional.empty();
            }
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
                
                ORDER BY urls.id DESC
                """;

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            var result = new ArrayList<UrlInfo>();

            while (rs.next()) {
                result.add(new UrlInfo(
                        mapUrl(rs),
                        mapUrlCheck(rs)
                ));
            }

            return result;
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
