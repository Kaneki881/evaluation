package ma.projet.service;

import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ma.projet.beans.*;

public class HommeService extends AbstractService<Homme> {
    public HommeService() { super(Homme.class); }

    public List<Femme> epousesEntre(int hommeId, LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Dates inversées");
        return read(s -> s.createQuery("select distinct m.femme from Mariage m " +
                "where m.homme.id = :id and m.dateDebut <= :fin " +
                "and (m.dateFin is null or m.dateFin >= :debut) order by m.femme.id", Femme.class)
            .setParameter("id", hommeId).setParameter("debut", debut).setParameter("fin", fin).getResultList());
    }

    // Quatre épouses distinctes : mariages commencés dans la période et encore en cours à sa fin.
    public long nombreHommesAvecQuatreEpouses(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Dates inversées");
        return read(s -> {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> m = cq.from(Mariage.class);
            Path<Integer> hommeId = m.get("homme").get("id");
            cq.select(hommeId).where(
                cb.between(m.get("dateDebut"), debut, fin),
                cb.or(cb.isNull(m.get("dateFin")), cb.greaterThanOrEqualTo(m.get("dateFin"), fin)))
                .groupBy(hommeId).having(cb.equal(cb.countDistinct(m.get("femme").get("id")), 4L));
            return (long) s.createQuery(cq).getResultList().size();
        });
    }

    public List<Mariage> mariagesDe(int hommeId) {
        return read(s -> s.createQuery("select m from Mariage m join fetch m.femme " +
                "where m.homme.id = :id order by m.dateDebut, m.id", Mariage.class)
            .setParameter("id", hommeId).getResultList());
    }
    public String afficherMariages(int hommeId) {
        Homme homme = findById(hommeId);
        if (homme == null) throw new IllegalArgumentException("Homme introuvable : " + hommeId);
        var fmt = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        List<Mariage> mariages = mariagesDe(hommeId);
        StringBuilder out = new StringBuilder("Nom : ").append(homme.getNom()).append(' ')
            .append(homme.getPrenom()).append("\nMariages En Cours :\n");
        int index = 1;
        for (Mariage m : mariages) if (m.getDateFin() == null) {
            out.append(index++).append(". Femme : ").append(m.getFemme().getPrenom()).append(' ')
                .append(m.getFemme().getNom()).append("   Date Début : ")
                .append(m.getDateDebut().format(fmt)).append("   Nbr Enfants : ")
                .append(m.getNbrEnfant()).append('\n');
        }
        out.append("\nMariages échoués :\n");
        index = 1;
        for (Mariage m : mariages) if (m.getDateFin() != null) {
            out.append(index++).append(". Femme : ").append(m.getFemme().getPrenom()).append(' ')
                .append(m.getFemme().getNom()).append("   Date Début : ")
                .append(m.getDateDebut().format(fmt)).append("\nDate Fin : ")
                .append(m.getDateFin().format(fmt)).append("   Nbr Enfants : ")
                .append(m.getNbrEnfant()).append('\n');
        }
        return out.toString();
    }
}
