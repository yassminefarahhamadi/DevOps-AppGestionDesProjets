package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Equipe;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.EquipeRepository;
import tn.esprit.backend.repository.ProjetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Test
    void addEquipe_shouldSaveAndReturn() {
        Equipe e = new Equipe();
        when(equipeRepository.save(e)).thenReturn(e);

        Equipe result = equipeService.addEquipe(e);

        assertSame(e, result);
        verify(equipeRepository, times(1)).save(e);
    }

    @Test
    void updateEquipe_shouldSaveAndReturn() {
        Equipe e = new Equipe();
        when(equipeRepository.save(e)).thenReturn(e);

        Equipe result = equipeService.updateEquipe(e);

        assertSame(e, result);
        verify(equipeRepository, times(1)).save(e);
    }

    @Test
    void deleteEquipe_shouldCallDeleteById() {
        equipeService.deleteEquipe(1L);

        verify(equipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void getEquipeById_found_shouldReturnEquipe() {
        Equipe e = new Equipe();
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(e));

        assertSame(e, equipeService.getEquipeById(1L));
    }

    @Test
    void getEquipeById_notFound_shouldReturnNull() {
        when(equipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(equipeService.getEquipeById(99L));
    }

    @Test
    void getAllEquipes_shouldReturnList() {
        when(equipeRepository.findAll()).thenReturn(List.of(new Equipe(), new Equipe()));

        List<Equipe> result = equipeService.getAllEquipes();

        assertEquals(2, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void getEquipesByEntreprise_shouldReturnList() {
        when(equipeRepository.findByEntrepriseId(3L)).thenReturn(List.of(new Equipe()));

        List<Equipe> result = equipeService.getEquipesByEntreprise(3L);

        assertEquals(1, result.size());
        verify(equipeRepository, times(1)).findByEntrepriseId(3L);
    }

    @Test
    void assignEquipeToEntreprise_shouldSetEntrepriseAndSave() {
        Equipe equipe = new Equipe();
        Entreprise entreprise = new Entreprise();
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(entrepriseRepository.findById(2L)).thenReturn(Optional.of(entreprise));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.assignEquipeToEntreprise(1L, 2L);

        assertSame(entreprise, result.getEntreprise());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void assignEquipeToEntreprise_equipeNotFound_shouldThrowNpe() {
        when(equipeRepository.findById(1L)).thenReturn(Optional.empty());

        // The service does not handle a missing Equipe: it throws NPE.
        assertThrows(NullPointerException.class,
                () -> equipeService.assignEquipeToEntreprise(1L, 2L));
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void assignEquipeToProjet_shouldAddProjetAndSave() {
        Equipe equipe = new Equipe();
        // If Equipe.projets is a Set in your entity, use: new HashSet<>()
        equipe.setProjets(new ArrayList<>());
        Projet projet = new Projet();
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(projetRepository.findById(2L)).thenReturn(Optional.of(projet));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.assignEquipeToProjet(1L, 2L);

        assertTrue(result.getProjets().contains(projet));
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void assignEquipeToProjet_equipeNotFound_shouldThrowNpe() {
        when(equipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class,
                () -> equipeService.assignEquipeToProjet(1L, 2L));
        verify(equipeRepository, never()).save(any());
    }
}