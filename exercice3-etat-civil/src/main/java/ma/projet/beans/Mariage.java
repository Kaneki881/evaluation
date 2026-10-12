package ma.projet.beans;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mariage")
public class Mariage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @ManyToOne(optional = false) @JoinColumn(name = "homme_id") private Homme homme;
    @ManyToOne(optional = false) @JoinColumn(name = "femme_id") private Femme femme;
    @Column(name = "date_debut", nullable = false) private LocalDate dateDebut;
    @Column(name = "date_fin") private LocalDate dateFin;
    @Column(name = "nbr_enfant", nullable = false) private int nbrEnfant;

    protected Mariage() {}
    public Mariage(Homme homme, Femme femme, LocalDate dateDebut, LocalDate dateFin, int nbrEnfant) {
        if (nbrEnfant < 0) throw new IllegalArgumentException("Nombre d'enfants négatif");
        if (dateFin != null && dateFin.isBefore(dateDebut)) throw new IllegalArgumentException("Dates inversées");
        this.homme = homme; this.femme = femme; this.dateDebut = dateDebut;
        this.dateFin = dateFin; this.nbrEnfant = nbrEnfant;
    }
    public Integer getId() { return id; }
    public Homme getHomme() { return homme; }
    public Femme getFemme() { return femme; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public int getNbrEnfant() { return nbrEnfant; }
    public void setNbrEnfant(int nbrEnfant) { if (nbrEnfant < 0) throw new IllegalArgumentException("Nombre d'enfants négatif"); this.nbrEnfant = nbrEnfant; }
}
