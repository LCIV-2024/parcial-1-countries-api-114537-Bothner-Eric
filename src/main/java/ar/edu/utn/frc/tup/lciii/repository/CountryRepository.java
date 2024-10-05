package ar.edu.utn.frc.tup.lciii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;

/**
 * Completar segun corresponda
 */
@Service
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
