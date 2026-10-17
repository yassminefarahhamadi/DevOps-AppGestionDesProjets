package tn.esprit.backend;

import org.junit.jupiter.api.Test;
import tn.esprit.backend.entity.Projet;

import static org.junit.jupiter.api.Assertions.*;

class ProjetTest {

    @Test
    void testSettersEtGetters() {
        Projet p = new Projet();
        p.setId(1L);
        p.setSujet("Gestion des projets");

        assertEquals(1L, p.getId());
        assertEquals("Gestion des projets", p.getSujet());
    }

    @Test
    void testConstructeurComplet() {
        Projet p = new Projet(2L, "DevOps", null, null);

        assertEquals(2L, p.getId());
        assertEquals("DevOps", p.getSujet());
    }

    @Test
    void testBuilder() {
        Projet p = Projet.builder().id(3L).sujet("Docker").build();

        assertNotNull(p);
        assertEquals("Docker", p.getSujet());
    }

    @Test
    void testModificationSujet() {
        Projet p = new Projet();
        p.setSujet("Ancien sujet");
        p.setSujet("Nouveau sujet");

        assertEquals("Nouveau sujet", p.getSujet());
        assertNotEquals("Ancien sujet", p.getSujet());
    }
}
