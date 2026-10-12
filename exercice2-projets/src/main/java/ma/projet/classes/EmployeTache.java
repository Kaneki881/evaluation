package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"employe_id", "tache_id"}))
public class EmployeTache {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @ManyToOne(optional = false) @JoinColumn(name = "employe_id") private Employe employe;
    @ManyToOne(optional = false) @JoinColumn(name = "tache_id") private Tache tache;
    @Column(nullable = false) private LocalDate dateDebutReelle;
    private LocalDate dateFinReelle;

    protected EmployeTache() {}
    public EmployeTache(Employe employe, Tache tache, LocalDate dateDebutReelle, LocalDate dateFinReelle) {
        if (dateFinReelle != null && dateFinReelle.isBefore(dateDebutReelle)) throw new IllegalArgumentException("Dates réelles inversées");
        this.employe = employe; this.tache = tache;
        this.dateDebutReelle = dateDebutReelle; this.dateFinReelle = dateFinReelle;
    }
    public Integer getId() { return id; }
    public Employe getEmploye() { return employe; }
    public Tache getTache() { return tache; }
    public LocalDate getDateDebutReelle() { return dateDebutReelle; }
    public void setDateDebutReelle(LocalDate dateDebutReelle) { this.dateDebutReelle = dateDebutReelle; }
    public LocalDate getDateFinReelle() { return dateFinReelle; }
    public void setDateFinReelle(LocalDate dateFinReelle) { this.dateFinReelle = dateFinReelle; }
}
