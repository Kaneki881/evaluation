package ma.projet;

import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import ma.projet.classes.*;
import ma.projet.service.*;
import org.junit.jupiter.api.Test;

class StockTest {
    static {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        System.setProperty("hibernate.connection.url", "jdbc:h2:mem:stock;MODE=MySQL;DB_CLOSE_DELAY=-1");
        System.setProperty("hibernate.connection.username", "sa");
        System.setProperty("hibernate.connection.password", "");
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }

    @Test void fonctionsDeStock() {
        var categories = new CategorieService();
        var produits = new ProduitService();
        var commandes = new CommandeService();
        var lignes = new LigneCommandeService();
        Categorie pc = categories.create(new Categorie("PC", "Ordinateurs"));
        Categorie periph = categories.create(new Categorie("PER", "Périphériques"));
        Produit p1 = produits.create(new Produit("ES12", new BigDecimal("120"), pc));
        Produit p2 = produits.create(new Produit("ZR85", new BigDecimal("100"), pc));
        Produit p3 = produits.create(new Produit("EE85", new BigDecimal("200"), periph));
        Commande c = commandes.create(new Commande(LocalDate.of(2013, 3, 14)));
        lignes.create(new LigneCommandeProduit(c, p1, 7));
        lignes.create(new LigneCommandeProduit(c, p2, 14));
        lignes.create(new LigneCommandeProduit(c, p3, 5));
        assertEquals(2, produits.produitsParCategorie(pc.getId()).size());
        assertEquals(3, produits.produitsCommandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31)).size());
        assertTrue(produits.produitsCommandesEntre(LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 30)).isEmpty());
        assertEquals(3, produits.produitsDeCommande(c.getId()).size());
        assertEquals(2, produits.produitsPrixSuperieurA100().size());
        String affichage = produits.afficherProduitsDeCommande(c.getId());
        assertTrue(affichage.contains("ES12   120 DH   7"));
        assertTrue(affichage.contains("ZR85   100 DH   14"));
        assertTrue(affichage.contains("EE85   200 DH   5"));
        assertEquals(3, produits.findAll().size());

        System.out.println("\n=== EXERCICE 1 : GESTION DE STOCK ===");
        System.out.println("Produits de la catégorie PC : " + produits.produitsParCategorie(pc.getId()).stream().map(Produit::getReference).toList());
        System.out.println("Produits commandés en mars 2013 : " + produits.produitsCommandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31)).stream().map(Produit::getReference).toList());
        System.out.println(affichage);
        System.out.println("Produits à plus de 100 DH : " + produits.produitsPrixSuperieurA100().stream().map(Produit::getReference).toList());

        p2.setPrix(new BigDecimal("110"));
        produits.update(p2);
        assertEquals(3, produits.produitsPrixSuperieurA100().size());
        assertEquals("PC", categories.findById(pc.getId()).getCode());
        Produit temporaire = produits.create(new Produit("TMP", BigDecimal.ONE, pc));
        produits.delete(temporaire);
        assertNull(produits.findById(temporaire.getId()));

    }
}
