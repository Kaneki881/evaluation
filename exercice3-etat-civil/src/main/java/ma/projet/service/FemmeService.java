package ma.projet.service;

import java.time.LocalDate;
import java.util.List;
import ma.projet.beans.Femme;

public class FemmeService extends AbstractService<Femme> {
    public FemmeService() { super(Femme.class); }

    public Femme plusAgee() {
        return read(s -> s.createQuery("from Femme f order by f.dateNaissance, f.id", Femme.class)
            .setMaxResults(1).uniqueResult());
    }
    // Le modèle stocke un total par mariage, sans dates de naissance des enfants.
    // La période sélectionne donc les mariages commencés entre ces deux dates.
    public long nombreEnfantsEntre(int femmeId, LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Dates inversées");
        Number value = read(s -> (Number) s.createNamedQuery("Femme.nombreEnfantsEntreDates", Long.class)
            .setParameter("femmeId", femmeId).setParameter("debut", debut).setParameter("fin", fin)
            .getSingleResult());
        return value.longValue();
    }
    public List<Femme> marieesAuMoinsDeuxFois() {
        return read(s -> s.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).getResultList());
    }
}
