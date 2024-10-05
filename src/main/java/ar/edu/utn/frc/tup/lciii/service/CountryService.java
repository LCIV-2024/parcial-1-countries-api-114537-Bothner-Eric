package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDTO;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private CountryRepository countryRepository;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<CountryDTO> getAllCountriesDTO() {
                return getAllCountries().stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByContinent(String continent) {
                return getAllCountries().stream()
                                .filter(country -> country.getRegion().toLowerCase().equals(continent.toLowerCase()))
                                .map(this::mapToDTO).collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesByLanguage(String language) {
                List<Country> countries = getAllCountries();

                List<CountryDTO> countriesWithLanguage = new ArrayList<>();

                for (Country country : countries) {
                        if (country.getLanguages() == null) {
                                continue;
                        }
                        for (Map.Entry<String, String> entry : country.getLanguages().entrySet()) {
                                if (entry.getValue().toLowerCase().equals(language.toLowerCase())) {
                                        countriesWithLanguage.add(mapToDTO(country));
                                }
                        }
                }

                return countriesWithLanguage;
        }

        public CountryDTO getCountryWithMostBorders() {
                List<Country> countries = getAllCountries();

                if (countries == null || countries.isEmpty()) {
                        return null;
                }

                if (countries.get(0).getBorders() == null) {
                        countries.get(0).setBorders(new ArrayList<>());
                }

                Country countryWithMostBorders = countries.get(0);

                for (Country country : countries) {
                        if (country.getBorders() == null) {
                                continue;
                        }
                        if (country.getBorders().size() > countryWithMostBorders.getBorders().size()) {
                                countryWithMostBorders = country;
                        }
                }

                return mapToDTO(countryWithMostBorders);
        }

        public List<CountryDTO> postCountries(PostCountryDTO postCountryDTO) {

                if (postCountryDTO.getAmountOfCountryToSave() > 10) {
                        return null;
                }

                List<Country> countries = getAllCountries();

                Collections.shuffle(countries);

                List<Country> filteredCountries = countries.stream().limit(postCountryDTO.getAmountOfCountryToSave()).collect(Collectors.toList());

                List<CountryEntity> countryEntities = new ArrayList<>();

                for (Country country : filteredCountries) {
                        CountryEntity countryEntity = new CountryEntity();
                        countryEntity.setName(country.getName());
                        countryEntity.setPopulation(country.getPopulation());
                        countryEntity.setArea(country.getArea());
                        countryEntity.setCode(country.getCode());
                        
                        countryEntities.add(countryEntity);
                }

                List<CountryEntity> entities = countryRepository.saveAll(countryEntities);

                List<CountryDTO> countryDTOS = new ArrayList<>();

                for (CountryEntity entity : entities) {
                        countryDTOS.add(new CountryDTO(entity.getCode(), entity.getName()));
                }

                return countryDTOS;

        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                                .name((String) nameData.get("common"))
                                .population(((Number) countryData.get("population")).longValue())
                                .area(((Number) countryData.get("area")).doubleValue())
                                .code((String) countryData.get("cca3"))
                                .region((String) countryData.get("region"))
                                .borders((List<String>) countryData.get("borders"))
                                .languages((Map<String, String>) countryData.get("languages"))
                                .build();
        }

        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }
}