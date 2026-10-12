package ma.projet;

import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import ma.projet.classes.*;
import ma.projet.service.*;
import org.junit.jupiter.api.Test;

class ProjetsTest {
    static {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        System.setProperty("hibernate.connection.url", "jdbc:h2:mem:projets;MODE=MySQL;DB_CLOSE_DELAY=-1");
        System.setProperty("hibernate.connection.username", "sa");
        System.setProperty("hibernate.connection.password", "");
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }
    private static LocalDate d(int year, int month, int day) { return LocalDate.of(year, month, day); }

    @Test void fonctionsDeProjets() {
        var employes = new EmployeService();
        var projets = new ProjetService();
        var taches = new TacheService();
        var affectations = new EmployeTacheService();
        Employe chef = employes.create(new Employe("Safi", "Said", "0600000000"));
        Employe analyste = employes.create(new Employe("Rami", "Salima", "0611111111"));
        Projet p = projets.create(new Projet("Gestion de stock", d(2013, 1, 14), d(2013, 12, 31), chef));
        Tache analyse = taches.create(new Tache("Analyse", d(2013, 2, 1), d(2013, 2, 28), new BigDecimal("1200"), p));
        Tache conception = taches.create(new Tache("Conception", d(2013, 3, 1), d(2013, 3, 31), new BigDecimal("1000"), p));
        Tache dev = taches.create(new Tache("Développement", d(2013, 4, 1), d(2013, 4, 30), new BigDecimal("2500"), p));
        affectations.create(new EmployeTache(analyste, analyse, d(2013, 2, 10), d(2013, 2, 20)));
        affectations.create(new EmployeTache(analyste, conception, d(2013, 3, 10), d(2013, 3, 15)));
        affectations.create(new EmployeTache(chef, dev, d(2013, 4, 10), d(2013, 4, 25)));
        assertEquals(2, employes.tachesRealiseesPar(analyste.getId()).size());
        assertEquals(1, employes.projetsGeresPar(chef.getId()).size());
        assertEquals(3, projets.tachesPlanifiees(p.getId()).size());
        assertEquals(3, projets.tachesRealiseesAvecDates(p.getId()).size());
        assertEquals(0, new BigDecimal("4700").compareTo(projets.coutGlobal(p.getId())));
        assertEquals(2, taches.tachesPrixSuperieurA1000().size());
        assertEquals(2, taches.tachesRealiseesEntre(d(2013, 2, 15), d(2013, 3, 12)).size());
        assertTrue(taches.tachesRealiseesEntre(d(2014, 1, 1), d(2014, 2, 1)).isEmpty());
        String affichage = projets.afficherTachesRealisees(p.getId());
        assertTrue(affichage.contains("Analyse 10/02/2013 20/02/2013"));
        assertTrue(affichage.contains("Développement 10/04/2013 25/04/2013"));
        assertEquals("Gestion de stock", projets.findById(p.getId()).getNom());
        assertEquals(2, employes.findAll().size());

        System.out.println("\n=== EXERCICE 2 : GESTION DE PROJETS ===");
        System.out.println("Tâches réalisées par l'analyste : " + employes.tachesRealiseesPar(analyste.getId()).stream().map(Tache::getNom).toList());
        System.out.println("Projets gérés par le chef : " + employes.projetsGeresPar(chef.getId()).stream().map(Projet::getNom).toList());
        System.out.println("Tâches planifiées : " + projets.tachesPlanifiees(p.getId()).stream().map(Tache::getNom).toList());
        System.out.println(affichage);
        System.out.println("Tâches à plus de 1000 DH : " + taches.tachesPrixSuperieurA1000().stream().map(Tache::getNom).toList());
        System.out.println("Tâches réalisées entre le 15/02 et le 12/03 : " + taches.tachesRealiseesEntre(d(2013, 2, 15), d(2013, 3, 12)).stream().map(Tache::getNom).toList());
        System.out.println("Coût global : " + projets.coutGlobal(p.getId()) + " DH");
    }
}
