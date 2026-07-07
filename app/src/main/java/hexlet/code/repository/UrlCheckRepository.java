package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = """
                INSERT INTO url_checks
                (status_code, title, h1, description, url_id, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var createdAt = LocalDateTime.now();

            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(createdAt));

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }


    public static List<UrlCheck> findAllByUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";

        var urlChecks = new ArrayList<UrlCheck>();

        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, urlId);

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var urlCheck = new UrlCheck(
                        resultSet.getLong("id"),
                        resultSet.getInt("status_code"),
                        resultSet.getString("title"),
                        resultSet.getString("h1"),
                        resultSet.getString("description"),
                        resultSet.getLong("url_id"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );

                urlChecks.add(urlCheck);
            }
        }

        return urlChecks;
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM url_checks";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
