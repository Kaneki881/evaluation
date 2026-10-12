package ma.projet.service;

import java.util.List;
import ma.projet.classes.*;

public class EmployeService extends AbstractService<Employe> {
    public EmployeService() { super(Employe.class); }

    public List<Tache> tachesRealiseesPar(int employeId) {
        return read(s -> s.createQuery("select et.tache from EmployeTache et where et.employe.id = :id " +
                "and et.dateFinReelle is not null order by et.tache.id", Tache.class)
            .setParameter("id", employeId).getResultList());
    }
    public List<Projet> projetsGeresPar(int employeId) {
        return read(s -> s.createQuery("from Projet p where p.chefProjet.id = :id order by p.id", Projet.class)
            .setParameter("id", employeId).getResultList());
    }
}
