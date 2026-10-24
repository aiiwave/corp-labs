package kz.narxoz.carrental.persistence;

import java.util.Optional;
import java.util.UUID;
import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RentalJdbc {

    private final JdbcTemplate jdbc;

    public RentalJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insert(UUID id, String businessKey, String status, String title) {
        jdbc.update("""
                INSERT INTO rental_request (id, business_key, status, title)
                VALUES (?, ?, ?, ?)
                """, id, businessKey, status, title);
    }

    public int count(String businessKey) {
        Integer n = jdbc.queryForObject(
                "SELECT count(*) FROM rental_request WHERE business_key = ?",
                Integer.class,
                businessKey);
        return n == null ? 0 : n;
    }

    public Optional<RentalStatus> findStatus(String businessKey) {
        return jdbc.query(
                "SELECT status FROM rental_request WHERE business_key = ?",
                (rs, i) -> toStatus(rs.getString("status")),
                businessKey).stream().findFirst();
    }

    private static RentalStatus toStatus(String text) {
        try {
            return RentalStatus.valueOf(text);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Unknown status in database: " + text, ex);
        }
    }
}
