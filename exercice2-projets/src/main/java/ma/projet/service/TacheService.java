package ma.projet.service;

import java.time.LocalDate;
import java.util.List;
import ma.projet.classes.Tache;

public class TacheService extends AbstractService<Tache> {
    public TacheService() { super(Tache.class); }
    public List<Tache> tachesPrixSuperieurA1000() {
        return read(s -> s.createNamedQuery("Tache.prixSuperieurA1000", Tache.class).getResultList());
    }
    public List<Tache> tachesRealiseesEntre(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Dates inversées");
        return read(s -> s.createQuery("select distinct et.tache from EmployeTache et " +
                "where et.dateFinReelle is not null and et.dateDebutReelle <= :fin " +
                "and et.dateFinReelle >= :debut order by et.tache.id", Tache.class)
            .setParameter("debut", debut).setParameter("fin", fin).getResultList());
    }
}
