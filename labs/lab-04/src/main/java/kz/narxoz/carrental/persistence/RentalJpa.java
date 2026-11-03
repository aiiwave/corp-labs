package kz.narxoz.carrental.persistence;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import kz.narxoz.carrental.domain.Rental;
import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RentalJpa {

    private final EntityManager em;

    public RentalJpa(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void insert(UUID id, String businessKey, String status, String title) {
        em.persist(new RentalEntity(id, businessKey, status, title));
        em.flush(); // the INSERT goes now, so a duplicate fails here and not at commit
    }

    public int count(String businessKey) {
        Long n = em.createQuery(
                        "select count(r) from RentalEntity r where r.businessKey = :key", Long.class)
                .setParameter("key", businessKey)
                .getSingleResult();
        return n.intValue();
    }

    /** Read by the UUID path id. Does not insert, update or flush. */
    public Optional<Rental> findById(UUID id) {
        return Optional.ofNullable(em.find(RentalEntity.class, id))
                .map(e -> new Rental(e.getId(), e.getBusinessKey(), toStatus(e.getStatus()), e.getTitle()));
    }

    /** Text column -> enum. Unknown text throws, it never becomes a new status. */
    public Optional<RentalStatus> findStatus(String businessKey) {
        return em.createQuery(
                        "select r.status from RentalEntity r where r.businessKey = :key", String.class)
                .setParameter("key", businessKey)
                .getResultStream()
                .findFirst()
                .map(RentalJpa::toStatus);
    }

    private static RentalStatus toStatus(String text) {
        try {
            return RentalStatus.valueOf(text);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Unknown status in database: " + text, ex);
        }
    }
}
