package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.entity.ProjetDetaille;
import tn.esprit.backend.repository.ProjetDetailleRepository;
import tn.esprit.backend.repository.ProjetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetDetailleServiceImplTest {

    @Mock
    private ProjetDetailleRepository projetDetailleRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetDetailleServiceImpl projetDetailleService;

    @Test
    void addProjetDetaille_shouldSaveAndReturn() {
        ProjetDetaille pd = new ProjetDetaille();
        when(projetDetailleRepository.save(pd)).thenReturn(pd);

        ProjetDetaille result = projetDetailleService.addProjetDetaille(pd);

        assertSame(pd, result);
        verify(projetDetailleRepository, times(1)).save(pd);
    }

    @Test
    void updateProjetDetaille_shouldSaveAndReturn() {
        ProjetDetaille pd = new ProjetDetaille();
        when(projetDetailleRepository.save(pd)).thenReturn(pd);

        ProjetDetaille result = projetDetailleService.updateProjetDetaille(pd);

        assertSame(pd, result);
        verify(projetDetailleRepository, times(1)).save(pd);
    }

    @Test
    void deleteProjetDetaille_shouldCallDeleteById() {
        projetDetailleService.deleteProjetDetaille(1L);

        verify(projetDetailleRepository, times(1)).deleteById(1L);
    }

    @Test
    void getProjetDetailleById_found_shouldReturnIt() {
        ProjetDetaille pd = new ProjetDetaille();
        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(pd));

        assertSame(pd, projetDetailleService.getProjetDetailleById(1L));
    }

    @Test
    void getProjetDetailleById_notFound_shouldReturnNull() {
        when(projetDetailleRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(projetDetailleService.getProjetDetailleById(99L));
    }

    @Test
    void getAllProjetsDetailles_shouldReturnList() {
        when(projetDetailleRepository.findAll())
                .thenReturn(List.of(new ProjetDetaille(), new ProjetDetaille()));

        List<ProjetDetaille> result = projetDetailleService.getAllProjetsDetailles();

        assertEquals(2, result.size());
        verify(projetDetailleRepository, times(1)).findAll();
    }

    @Test
    void getProjetDetaillesByProjet_shouldReturnList() {
        when(projetDetailleRepository.findByProjetId(5L))
                .thenReturn(List.of(new ProjetDetaille()));

        List<ProjetDetaille> result = projetDetailleService.getProjetDetaillesByProjet(5L);

        assertEquals(1, result.size());
        verify(projetDetailleRepository, times(1)).findByProjetId(5L);
    }

    @Test
    void assignProjetDetailleToProjet_shouldSetProjetAndSave() {
        ProjetDetaille pd = new ProjetDetaille();
        Projet projet = new Projet();
        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(pd));
        when(projetRepository.findById(2L)).thenReturn(Optional.of(projet));
        when(projetDetailleRepository.save(pd)).thenReturn(pd);

        ProjetDetaille result = projetDetailleService.assignProjetDetailleToProjet(1L, 2L);

        assertSame(projet, result.getProjet());
        verify(projetDetailleRepository, times(1)).save(pd);
    }

    @Test
    void assignProjetDetailleToProjet_projetDetailleNotFound_shouldThrowNpe() {
        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.empty());

        // The service does not handle a missing ProjetDetaille: it throws NPE.
        assertThrows(NullPointerException.class,
                () -> projetDetailleService.assignProjetDetailleToProjet(1L, 2L));
        verify(projetDetailleRepository, never()).save(any());
    }
}
