package ar.edu.utn.frc.tup.lciii.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import ar.edu.utn.frc.tup.lciii.service.CountryService;

@SpringBootTest
public class CountryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @Test
    public void testGetAllCountries() {

        List<Map<String, Object>> mockResponse = new ArrayList<>();

        Map<String, Object> countryData = new HashMap<>();
        Map<String, Object> nameData = new HashMap<>();
        nameData.put("common", "Argentina");
        countryData.put("name", nameData);
        countryData.put("population", 45195777L);
        countryData.put("area", 2780400.0);
        countryData.put("cca3", "ARG");
        countryData.put("region", "Americas");
        countryData.put("borders", Arrays.asList("BRA", "CHL"));
        countryData.put("languages", Collections.singletonMap("spa", "Spanish"));

        mockResponse.add(countryData);

        when(restTemplate.getForObject(anyString(), Mockito.eq(List.class))).thenReturn(mockResponse);

        List<Country> countries = countryService.getAllCountries();

        assertEquals(1, countries.size());
        Country country = countries.get(0);
        assertEquals("Argentina", country.getName());
        assertEquals(45195777L, country.getPopulation());
        assertEquals(2780400.0, country.getArea());
        assertEquals("ARG", country.getCode());
        assertEquals("Americas", country.getRegion());
        assertEquals(Arrays.asList("BRA", "CHL"), country.getBorders());
        assertEquals(Collections.singletonMap("spa", "Spanish"), country.getLanguages());
    }

    @Test
    public void testGetCountriesByContinent() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();

        Map<String, Object> countryData1 = new HashMap<>();
        Map<String, Object> nameData1 = new HashMap<>();
        nameData1.put("common", "Argentina");
        countryData1.put("name", nameData1);
        countryData1.put("population", 45195777L);
        countryData1.put("area", 2780400.0);
        countryData1.put("cca3", "ARG");
        countryData1.put("region", "Americas");
        countryData1.put("borders", Arrays.asList("BRA", "CHL"));
        countryData1.put("languages", Collections.singletonMap("spa", "Spanish"));

        Map<String, Object> countryData2 = new HashMap<>();
        Map<String, Object> nameData2 = new HashMap<>();
        nameData2.put("common", "Brazil");
        countryData2.put("name", nameData2);
        countryData2.put("population", 211000000L);
        countryData2.put("area", 8515767.0);
        countryData2.put("cca3", "BRA");
        countryData2.put("region", "Americas");
        countryData2.put("borders", Arrays.asList("ARG", "CHL"));
        countryData2.put("languages", Collections.singletonMap("por", "Portuguese"));

        mockResponse.add(countryData1);
        mockResponse.add(countryData2);

        when(restTemplate.getForObject(anyString(), Mockito.eq(List.class))).thenReturn(mockResponse);

        List<CountryDTO> countriesInAmericas = countryService.getCountriesByContinent("Americas");

        assertEquals(2, countriesInAmericas.size());
        assertTrue(countriesInAmericas.stream().anyMatch(countryDTO -> countryDTO.getName().equals("Argentina")));
        assertTrue(countriesInAmericas.stream().anyMatch(countryDTO -> countryDTO.getName().equals("Brazil")));

        List<CountryDTO> countriesInInvalidContinent = countryService.getCountriesByContinent("Europe");
        assertTrue(countriesInInvalidContinent.isEmpty(), "No se encontraron paises de europa en la lista.");
    }

    @Test
    public void testGetCountriesByLanguage() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> countryData1 = new HashMap<>();
        Map<String, Object> nameData1 = new HashMap<>();
        nameData1.put("common", "Argentina");
        countryData1.put("name", nameData1);
        countryData1.put("population", 45195777L);
        countryData1.put("area", 2780400.0);
        countryData1.put("cca3", "ARG");
        countryData1.put("region", "Americas");
        countryData1.put("borders", Arrays.asList("BRA", "CHL"));
        countryData1.put("languages", Collections.singletonMap("spa", "Spanish"));

        Map<String, Object> countryData2 = new HashMap<>();
        Map<String, Object> nameData2 = new HashMap<>();
        nameData2.put("common", "Brazil");
        countryData2.put("name", nameData2);
        countryData2.put("population", 211000000L);
        countryData2.put("area", 8515767.0);
        countryData2.put("cca3", "BRA");
        countryData2.put("region", "Americas");
        countryData2.put("borders", Arrays.asList("ARG", "CHL"));
        countryData2.put("languages", Collections.singletonMap("por", "Portuguese"));

        mockResponse.add(countryData1);
        mockResponse.add(countryData2);

        when(restTemplate.getForObject(anyString(), Mockito.eq(List.class))).thenReturn(mockResponse);

        List<CountryDTO> countriesWithSpanish = countryService.getCountriesByLanguage("Spanish");

        assertEquals(1, countriesWithSpanish.size());
        assertEquals("Argentina", countriesWithSpanish.get(0).getName());

        List<CountryDTO> countriesWithPortuguese = countryService.getCountriesByLanguage("Portuguese");

        assertEquals(1, countriesWithPortuguese.size());
        assertEquals("Brazil", countriesWithPortuguese.get(0).getName());

        List<CountryDTO> countriesWithInvalidLanguage = countryService.getCountriesByLanguage("French");
        assertTrue(countriesWithInvalidLanguage.isEmpty(), "No se encontraron paises que hablen frances");
    }

    @Test
    public void testGetCountryWithMostBorders() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();

        Map<String, Object> countryData1 = new HashMap<>();
        Map<String, Object> nameData1 = new HashMap<>();
        nameData1.put("common", "Argentina");
        countryData1.put("name", nameData1);
        countryData1.put("population", 45195777L);
        countryData1.put("area", 2780400.0);
        countryData1.put("cca3", "ARG");
        countryData1.put("region", "Americas");
        countryData1.put("borders", Arrays.asList("BRA", "CHL"));
        countryData1.put("languages", Collections.singletonMap("spa", "Spanish"));

        Map<String, Object> countryData2 = new HashMap<>();
        Map<String, Object> nameData2 = new HashMap<>();
        nameData2.put("common", "Brazil");
        countryData2.put("name", nameData2);
        countryData2.put("population", 211000000L);
        countryData2.put("area", 8515767.0);
        countryData2.put("cca3", "BRA");
        countryData2.put("region", "Americas");
        countryData2.put("borders", Arrays.asList("ARG", "CHL", "PRY", "URY"));
        countryData2.put("languages", Collections.singletonMap("por", "Portuguese"));

        Map<String, Object> countryData3 = new HashMap<>();
        Map<String, Object> nameData3 = new HashMap<>();
        nameData3.put("common", "Chile");
        countryData3.put("name", nameData3);
        countryData3.put("population", 19116201L);
        countryData3.put("area", 756102.0);
        countryData3.put("cca3", "CHL");
        countryData3.put("region", "Americas");
        countryData3.put("borders", Arrays.asList("ARG", "PER"));
        countryData3.put("languages", Collections.singletonMap("spa", "Spanish"));

        mockResponse.add(countryData1);
        mockResponse.add(countryData2);
        mockResponse.add(countryData3);

        when(restTemplate.getForObject(anyString(), Mockito.eq(List.class))).thenReturn(mockResponse);

        CountryDTO countryWithMostBorders = countryService.getCountryWithMostBorders();

        assertNotNull(countryWithMostBorders, "Se encontro el pais con mas fronteras");
        assertEquals("Brazil", countryWithMostBorders.getName(),
                "Brazil es el pais con mas fronteras");
    }

    @Test
    public void testGetAllCountriesDTO() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();

        Map<String, Object> countryData1 = new HashMap<>();
        Map<String, Object> nameData1 = new HashMap<>();
        nameData1.put("common", "Argentina");
        countryData1.put("name", nameData1);
        countryData1.put("population", 45195777L);
        countryData1.put("area", 2780400.0);
        countryData1.put("cca3", "ARG");
        countryData1.put("region", "Americas");
        countryData1.put("borders", Arrays.asList("BRA", "CHL"));
        countryData1.put("languages", Collections.singletonMap("spa", "Spanish"));

        Map<String, Object> countryData2 = new HashMap<>();
        Map<String, Object> nameData2 = new HashMap<>();
        nameData2.put("common", "Brazil");
        countryData2.put("name", nameData2);
        countryData2.put("population", 211000000L);
        countryData2.put("area", 8515767.0);
        countryData2.put("cca3", "BRA");
        countryData2.put("region", "Americas");
        countryData2.put("borders", Arrays.asList("ARG", "CHL"));
        countryData2.put("languages", Collections.singletonMap("por", "Portuguese"));

        mockResponse.add(countryData1);
        mockResponse.add(countryData2);

        when(restTemplate.getForObject(anyString(), Mockito.eq(List.class))).thenReturn(mockResponse);

        List<CountryDTO> countriesDTO = countryService.getAllCountriesDTO();

        assertEquals(2, countriesDTO.size(), "2 paises en la lista");

        CountryDTO argentinaDTO = countriesDTO.stream()
                .filter(countryDTO -> countryDTO.getCode().equals("ARG"))
                .findFirst()
                .orElse(null);

        assertNotNull(argentinaDTO, "Argentina debe estar en la lista");
        assertEquals("Argentina", argentinaDTO.getName());

        CountryDTO brazilDTO = countriesDTO.stream()
                .filter(countryDTO -> countryDTO.getCode().equals("BRA"))
                .findFirst()
                .orElse(null);

        assertNotNull(brazilDTO, "Brazil esta en la lista");
        assertEquals("Brazil", brazilDTO.getName());
    }

    @Test
    public void testPostCountriesMoreThan10() {
        PostCountryDTO postCountryDTO = new PostCountryDTO();
        postCountryDTO.setAmountOfCountryToSave(11L);

        List<CountryDTO> countryDTOs = countryService.postCountries(postCountryDTO);

        assertEquals(null, countryDTOs);
    }

}
