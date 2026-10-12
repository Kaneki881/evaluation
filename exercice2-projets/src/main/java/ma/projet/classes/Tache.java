package ma.projet.classes;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "Tache.prixSuperieurA1000", query = "from Tache t where t.prix > 1000 order by t.id")
public class Tache {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false) private String nom;
    @Column(nullable = false) private LocalDate dateDebut;
    @Column(nullable = false) private LocalDate dateFin;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal prix;
    @ManyToOne(optional = false) @JoinColumn(name = "projet_id") private Projet projet;

    protected Tache() {}
    public Tache(String nom, LocalDate dateDebut, LocalDate dateFin, BigDecimal prix, Projet projet) {
        this.nom = nom; this.dateDebut = dateDebut; this.dateFin = dateFin; this.prix = prix; this.projet = projet;
    }
    public Integer getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
}
