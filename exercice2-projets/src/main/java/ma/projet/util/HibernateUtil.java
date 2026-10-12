package ma.projet.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import ma.projet.classes.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.MetadataSources;

public final class HibernateUtil {
    private static final SessionFactory FACTORY = build();
    private HibernateUtil() {}
    private static SessionFactory build() {
        Properties settings = new Properties();
        try (InputStream in = HibernateUtil.class.getResourceAsStream("/application.properties")) {
            if (in == null) throw new IllegalStateException("application.properties introuvable");
            settings.load(in);
        } catch (IOException e) { throw new IllegalStateException("Configuration Hibernate illisible", e); }
        for (String key : settings.stringPropertyNames()) {
            String override = System.getProperty(key);
            if (override != null) settings.setProperty(key, override);
        }
        var registry = new StandardServiceRegistryBuilder().applySettings(settings).build();
        try {
            return new MetadataSources(registry)
                .addAnnotatedClass(Employe.class).addAnnotatedClass(Projet.class)
                .addAnnotatedClass(Tache.class).addAnnotatedClass(EmployeTache.class)
                .buildMetadata().buildSessionFactory();
        } catch (RuntimeException e) {
            StandardServiceRegistryBuilder.destroy(registry); throw e;
        }
    }
    public static SessionFactory getSessionFactory() { return FACTORY; }
    public static void shutdown() { FACTORY.close(); }
}
