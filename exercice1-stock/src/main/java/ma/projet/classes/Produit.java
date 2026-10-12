package ma.projet.classes;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@NamedQuery(name = "Produit.prixSuperieurA100", query = "select p from Produit p where p.prix > 100 order by p.reference")
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false, unique = true) private String reference;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal prix;
    @ManyToOne(optional = false) @JoinColumn(name = "categorie_id") private Categorie categorie;

    protected Produit() {}
    public Produit(String reference, BigDecimal prix, Categorie categorie) {
        this.reference = reference; this.prix = prix; this.categorie = categorie;
    }
    public Integer getId() { return id; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
}
