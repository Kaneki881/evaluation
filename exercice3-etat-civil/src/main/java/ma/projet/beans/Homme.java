package ma.projet.beans;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Homme extends Personne {
    protected Homme() {}
    public Homme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }
}
