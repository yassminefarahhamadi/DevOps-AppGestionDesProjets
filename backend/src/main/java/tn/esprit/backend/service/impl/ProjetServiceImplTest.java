package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.ProjetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetServiceImplTest {

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetServiceImpl projetService;

    @Test
    void addProjet_shouldSaveAndReturn() {
        Projet p = new Projet();
        when(projetRepository.save(p)).thenReturn(p);

        Projet result = projetService.addProjet(p);

        assertSame(p, result);
        verify(projetRepository, times(1)).save(p);
    }

    @Test
    void updateProjet_shouldSaveAndReturn() {
        Projet p = new Projet();
        when(projetRepository.save(p)).thenReturn(p);

        Projet result = projetService.updateProjet(p);

        assertSame(p, result);
        verify(projetRepository, times(1)).save(p);
    }

    @Test
    void deleteProjet_shouldCallDeleteById() {
        projetService.deleteProjet(1L);

        verify(projetRepository, times(1)).deleteById(1L);
    }

    @Test
    void getProjetById_found_shouldReturnProjet() {
        Projet p = new Projet();
        when(projetRepository.findById(1L)).thenReturn(Optional.of(p));

        assertSame(p, projetService.getProjetById(1L));
    }

    @Test
    void getProjetById_notFound_shouldReturnNull() {
        when(projetRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(projetService.getProjetById(99L));
    }

    @Test
    void getAllProjets_shouldReturnList() {
        when(projetRepository.findAll()).thenReturn(List.of(new Projet(), new Projet()));

        List<Projet> result = projetService.getAllProjets();

        assertEquals(2, result.size());
        verify(projetRepository, times(1)).findAll();
    }
}