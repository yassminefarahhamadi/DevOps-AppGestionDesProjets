package tn.esprit.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceImplTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;

    @Test
    void addEntreprise_shouldSaveAndReturn() {
        Entreprise e = new Entreprise();
        when(entrepriseRepository.save(e)).thenReturn(e);

        Entreprise result = entrepriseService.addEntreprise(e);

        assertSame(e, result);
        verify(entrepriseRepository, times(1)).save(e);
    }

    @Test
    void updateEntreprise_shouldSaveAndReturn() {
        Entreprise e = new Entreprise();
        when(entrepriseRepository.save(e)).thenReturn(e);

        Entreprise result = entrepriseService.updateEntreprise(e);

        assertSame(e, result);
        verify(entrepriseRepository, times(1)).save(e);
    }

    @Test
    void deleteEntreprise_shouldCallDeleteById() {
        entrepriseService.deleteEntreprise(1L);

        verify(entrepriseRepository, times(1)).deleteById(1L);
    }

    @Test
    void getEntrepriseById_found_shouldReturnEntreprise() {
        Entreprise e = new Entreprise();
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(e));

        assertSame(e, entrepriseService.getEntrepriseById(1L));
    }

    @Test
    void getEntrepriseById_notFound_shouldReturnNull() {
        when(entrepriseRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(entrepriseService.getEntrepriseById(99L));
    }

    @Test
    void getAllEntreprises_shouldReturnList() {
        when(entrepriseRepository.findAll())
                .thenReturn(List.of(new Entreprise(), new Entreprise()));

        List<Entreprise> result = entrepriseService.getAllEntreprises();

        assertEquals(2, result.size());
        verify(entrepriseRepository, times(1)).findAll();
    }
}