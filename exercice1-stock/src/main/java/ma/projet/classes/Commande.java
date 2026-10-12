package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Commande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false) private LocalDate date;

    protected Commande() {}
    public Commande(LocalDate date) { this.date = date; }
    public Integer getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
