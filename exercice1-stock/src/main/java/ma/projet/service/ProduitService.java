package ma.projet.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import ma.projet.classes.*;

public class ProduitService extends AbstractService<Produit> {
    public ProduitService() { super(Produit.class); }

    public List<Produit> produitsParCategorie(int categorieId) {
        return read(s -> s.createQuery("from Produit p where p.categorie.id = :id order by p.reference", Produit.class)
            .setParameter("id", categorieId).getResultList());
    }

    public List<Produit> produitsCommandesEntre(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) throw new IllegalArgumentException("Dates inversées");
        return read(s -> s.createQuery("select distinct l.produit from LigneCommandeProduit l " +
                "where l.commande.date between :debut and :fin order by l.produit.reference", Produit.class)
            .setParameter("debut", debut).setParameter("fin", fin).getResultList());
    }

    public List<LigneCommandeProduit> produitsDeCommande(int commandeId) {
        return read(s -> s.createQuery("select l from LigneCommandeProduit l join fetch l.produit " +
                "where l.commande.id = :id order by l.produit.reference", LigneCommandeProduit.class)
            .setParameter("id", commandeId).getResultList());
    }

    public String afficherProduitsDeCommande(int commandeId) {
        Commande commande = new CommandeService().findById(commandeId);
        if (commande == null) throw new IllegalArgumentException("Commande introuvable : " + commandeId);
        var fmt = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
        StringBuilder out = new StringBuilder("Commande : ").append(commandeId).append("     Date : ")
            .append(commande.getDate().format(fmt)).append("\nListe des produits :\nRéférence   Prix    Quantité\n");
        for (var ligne : produitsDeCommande(commandeId)) {
            out.append(ligne.getProduit().getReference()).append("   ")
                .append(ligne.getProduit().getPrix().stripTrailingZeros().toPlainString())
                .append(" DH   ").append(ligne.getQuantite()).append('\n');
        }
        return out.toString();
    }

    public List<Produit> produitsPrixSuperieurA100() {
        return read(s -> s.createNamedQuery("Produit.prixSuperieurA100", Produit.class).getResultList());
    }
}
