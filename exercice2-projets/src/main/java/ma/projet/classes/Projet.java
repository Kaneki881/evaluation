package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Projet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false) private String nom;
    @Column(nullable = false) private LocalDate dateDebut;
    private LocalDate dateFin;
    @ManyToOne(optional = false) @JoinColumn(name = "chef_projet_id") private Employe chefProjet;

    protected Projet() {}
    public Projet(String nom, LocalDate dateDebut, LocalDate dateFin, Employe chefProjet) {
        this.nom = nom; this.dateDebut = dateDebut; this.dateFin = dateFin; this.chefProjet = chefProjet;
    }
    public Integer getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public Employe getChefProjet() { return chefProjet; }
    public void setChefProjet(Employe chefProjet) { this.chefProjet = chefProjet; }
}
