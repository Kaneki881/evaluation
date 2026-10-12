package ma.projet.classes;

import jakarta.persistence.*;

@Entity
public class Categorie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true) private String code;
    @Column(nullable = false) private String libelle;

    protected Categorie() {}
    public Categorie(String code, String libelle) { this.code = code; this.libelle = libelle; }
    public Integer getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}
