package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    private static UrlCheck mapUrlCheck(ResultSet rs) throws SQLException {
        return new UrlCheck(
                rs.getLong("id"),
                rs.getInt("status_code"),
                rs.getString("title"),
                rs.getString("h1"),
                rs.getString("description"),
                rs.getLong("url_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = """
                INSERT INTO url_checks
                (status_code, title, h1, description, url_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, urlCheck.getStatusCode());
            stmt.setString(2, urlCheck.getTitle());
            stmt.setString(3, urlCheck.getH1());
            stmt.setString(4, urlCheck.getDescription());
            stmt.setLong(5, urlCheck.getUrlId());

            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("DB has not returned an id after saving an entity");
                }

                urlCheck.setId(generatedKeys.getLong(1));
            }
        }
    }

    public static List<UrlCheck> findAllByUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";

        var checks = new ArrayList<UrlCheck>();

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    checks.add(mapUrlCheck(rs));
                }
            }
        }

        return checks;
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM url_checks";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
