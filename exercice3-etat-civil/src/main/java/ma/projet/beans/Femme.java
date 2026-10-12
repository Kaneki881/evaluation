package ma.projet.beans;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "Femme.marieesAuMoinsDeuxFois",
    query = "select m.femme from Mariage m group by m.femme having count(m.id) >= 2")
@NamedNativeQuery(name = "Femme.nombreEnfantsEntreDates",
    query = "select coalesce(sum(m.nbr_enfant), 0) from mariage m " +
            "where m.femme_id = :femmeId and m.date_debut between :debut and :fin",
    resultClass = Long.class)
public class Femme extends Personne {
    protected Femme() {}
    public Femme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}
