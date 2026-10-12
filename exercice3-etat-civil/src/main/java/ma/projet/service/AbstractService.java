package ma.projet.service;

import java.util.List;
import java.util.function.Function;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

abstract class AbstractService<T> implements IDao<T> {
    private final Class<T> type;
    protected AbstractService(Class<T> type) { this.type = type; }
    protected <R> R read(Function<Session, R> work) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) { return work.apply(session); }
    }
    protected <R> R write(Function<Session, R> work) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            try { R result = work.apply(session); tx.commit(); return result; }
            catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public T create(T entity) { return write(s -> { s.persist(entity); return entity; }); }
    public T update(T entity) { return write(s -> s.merge(entity)); }
    public void delete(T entity) { write(s -> { s.remove(s.contains(entity) ? entity : s.merge(entity)); return null; }); }
    public T findById(int id) { return read(s -> s.find(type, id)); }
    public List<T> findAll() { return read(s -> s.createQuery("from " + type.getSimpleName(), type).getResultList()); }
}
