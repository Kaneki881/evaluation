package ma.projet.service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import ma.projet.classes.*;

public class ProjetService extends AbstractService<Projet> {
    public ProjetService() { super(Projet.class); }

    public List<Tache> tachesPlanifiees(int projetId) {
        return read(s -> s.createQuery("from Tache t where t.projet.id = :id order by t.dateDebut, t.id", Tache.class)
            .setParameter("id", projetId).getResultList());
    }
    public List<EmployeTache> tachesRealiseesAvecDates(int projetId) {
        return read(s -> s.createQuery("select et from EmployeTache et join fetch et.tache t " +
                "where t.projet.id = :id and et.dateFinReelle is not null order by et.dateDebutReelle, t.id", EmployeTache.class)
            .setParameter("id", projetId).getResultList());
    }
    public BigDecimal coutGlobal(int projetId) {
        return tachesPlanifiees(projetId).stream().map(Tache::getPrix).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public String afficherTachesRealisees(int projetId) {
        Projet p = findById(projetId);
        if (p == null) throw new IllegalArgumentException("Projet introuvable : " + projetId);
        var longFmt = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
        var fmt = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        StringBuilder out = new StringBuilder("Projet : ").append(p.getId()).append("    Nom : ")
            .append(p.getNom()).append("    Date début : ").append(p.getDateDebut().format(longFmt))
            .append("\nListe des tâches :\nNum Nom Date Début Réelle Date Fin Réelle\n");
        for (var et : tachesRealiseesAvecDates(projetId)) {
            out.append(et.getTache().getId()).append(' ').append(et.getTache().getNom()).append(' ')
                .append(et.getDateDebutReelle().format(fmt)).append(' ')
                .append(et.getDateFinReelle().format(fmt)).append('\n');
        }
        return out.toString();
    }
}
