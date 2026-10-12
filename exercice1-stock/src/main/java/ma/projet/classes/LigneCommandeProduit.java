package ma.projet.classes;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"commande_id", "produit_id"}))
public class LigneCommandeProduit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @ManyToOne(optional = false) @JoinColumn(name = "commande_id") private Commande commande;
    @ManyToOne(optional = false) @JoinColumn(name = "produit_id") private Produit produit;
    @Column(nullable = false) private int quantite;

    protected LigneCommandeProduit() {}
    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.commande = commande; this.produit = produit; this.quantite = quantite;
    }
    public Integer getId() { return id; }
    public Commande getCommande() { return commande; }
    public Produit getProduit() { return produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.quantite = quantite;
    }
}
