package ma.projet;

import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import ma.projet.beans.*;
import ma.projet.service.*;
import org.junit.jupiter.api.Test;

class EtatCivilTest {
    static {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        System.setProperty("hibernate.connection.url", "jdbc:h2:mem:etatcivil;MODE=MySQL;DB_CLOSE_DELAY=-1");
        System.setProperty("hibernate.connection.username", "sa");
        System.setProperty("hibernate.connection.password", "");
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }
    private static LocalDate d(int year, int month, int day) { return LocalDate.of(year, month, day); }

    @Test void fonctionsEtatCivil() {
        var femmes = new FemmeService();
        var hommes = new HommeService();
        var mariages = new MariageService();
        Homme[] hs = new Homme[5];
        Femme[] fs = new Femme[10];
        for (int i = 0; i < hs.length; i++) hs[i] = hommes.create(new Homme("H" + i, "Said", "0600", "Rabat", d(1970 + i, 1, 1)));
        for (int i = 0; i < fs.length; i++) fs[i] = femmes.create(new Femme("F" + i, "Salima", "0611", "Rabat", d(1960 + i, 2, 2)));
        assertEquals(10, femmes.findAll().size());
        assertEquals(fs[0].getId(), femmes.plusAgee().getId());
        mariages.create(new Mariage(hs[0], fs[4], d(1989, 9, 3), d(1990, 9, 3), 0));
        mariages.create(new Mariage(hs[0], fs[0], d(1990, 9, 3), null, 4));
        mariages.create(new Mariage(hs[0], fs[1], d(1995, 9, 3), null, 2));
        mariages.create(new Mariage(hs[0], fs[2], d(2000, 11, 4), null, 3));
        mariages.create(new Mariage(hs[0], fs[3], d(2002, 1, 1), null, 1));
        mariages.create(new Mariage(hs[1], fs[4], d(1994, 1, 1), null, 2));
        assertEquals(4, hommes.epousesEntre(hs[0].getId(), d(1991, 1, 1), d(2003, 1, 1)).size());
        assertEquals(0, hommes.epousesEntre(hs[0].getId(), d(1980, 1, 1), d(1988, 1, 1)).size());
        assertEquals(2, femmes.nombreEnfantsEntre(fs[4].getId(), d(1990, 1, 1), d(2000, 1, 1)));
        assertTrue(femmes.marieesAuMoinsDeuxFois().stream().anyMatch(f -> f.getId().equals(fs[4].getId())));
        assertEquals(1, hommes.nombreHommesAvecQuatreEpouses(d(1990, 1, 1), d(2003, 1, 1)));
        assertEquals(5, hommes.mariagesDe(hs[0].getId()).size());
        String affichage = hommes.afficherMariages(hs[0].getId());
        assertTrue(affichage.contains("Mariages En Cours"));
        assertTrue(affichage.contains("Nbr Enfants : 4"));
        assertTrue(affichage.contains("Mariages échoués"));
        assertTrue(affichage.contains("Date Fin : 03/09/1990"));
        assertEquals(5, hommes.findAll().size());

        System.out.println("\n=== EXERCICE 3 : ÉTAT CIVIL ===");
        System.out.println("Femmes : " + femmes.findAll().stream().map(Femme::getNom).toList());
        System.out.println("Femme la plus âgée : " + femmes.plusAgee().getNom());
        System.out.println("Épouses entre 1991 et 2003 : " + hommes.epousesEntre(hs[0].getId(), d(1991, 1, 1), d(2003, 1, 1)).stream().map(Femme::getNom).toList());
        System.out.println("Enfants de F4 pour les mariages commencés entre 1990 et 2000 : " + femmes.nombreEnfantsEntre(fs[4].getId(), d(1990, 1, 1), d(2000, 1, 1)));
        System.out.println("Femmes mariées au moins deux fois : " + femmes.marieesAuMoinsDeuxFois().stream().map(Femme::getNom).toList());
        System.out.println("Hommes ayant quatre épouses : " + hommes.nombreHommesAvecQuatreEpouses(d(1990, 1, 1), d(2003, 1, 1)));
        System.out.println(affichage);
    }
}
