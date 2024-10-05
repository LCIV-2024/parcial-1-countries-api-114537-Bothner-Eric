package ar.edu.utn.frc.tup.lciii.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void getCountries() throws Exception {

        List<CountryDTO> countries = new ArrayList<>();

        countries.add(new CountryDTO("ARG", "Argentina"));
        countries.add(new CountryDTO("BRA", "Brazil"));

        when(countryService.getAllCountriesDTO()).thenReturn(countries);

        this.mockMvc.perform(get("/api/countries")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("ARG")).andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].code").value("BRA")).andExpect(jsonPath("$[1].name").value("Brazil"));

    }

    @Test
    public void getCountriesByName() throws Exception {

        List<CountryDTO> countries = new ArrayList<>();

        countries.add(new CountryDTO("ARG", "Argentina"));
        countries.add(new CountryDTO("BRA", "Brazil"));

        when(countryService.getAllCountriesDTO()).thenReturn(countries);

        this.mockMvc.perform(get("/api/countries?name=arg")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("ARG")).andExpect(jsonPath("$[0].name").value("Argentina"));

    }

    @Test
    public void getCountriesByCode() throws Exception {

        List<CountryDTO> countries = new ArrayList<>();

        countries.add(new CountryDTO("ARG", "Argentina"));
        countries.add(new CountryDTO("BRA", "Brazil"));

        when(countryService.getAllCountriesDTO()).thenReturn(countries);

        this.mockMvc.perform(get("/api/countries?code=bra")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("BRA")).andExpect(jsonPath("$[0].name").value("Brazil"));

    }

    @Test
    public void getCountriesByContinent() throws Exception {

        List<CountryDTO> countries = new ArrayList<>();

        countries.add(new CountryDTO("ARG", "Argentina"));
        countries.add(new CountryDTO("BRA", "Brazil"));

        when(countryService.getCountriesByContinent("America")).thenReturn(countries);

        this.mockMvc.perform(get("/api/countries/America/continent")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("ARG")).andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].code").value("BRA")).andExpect(jsonPath("$[1].name").value("Brazil"));

    }

    @Test
    public void getCountriesByLanguage() throws Exception {

        List<CountryDTO> countries = new ArrayList<>();

        countries.add(new CountryDTO("ARG", "Argentina"));
        countries.add(new CountryDTO("BRA", "Brazil"));

        when(countryService.getCountriesByLanguage("Spanish")).thenReturn(countries);

        this.mockMvc.perform(get("/api/countries/Spanish/language")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("ARG")).andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].code").value("BRA")).andExpect(jsonPath("$[1].name").value("Brazil"));

    }

    @Test
    public void getCountryWithMostBorders() throws Exception {

        List<Country> countries = new ArrayList<>();

        countries.add(Country.builder().code("ARG").name("Argentina").borders(List.of("BRA", "CHI")).build());
        countries.add(Country.builder().code("BRA").name("Brazil").borders(List.of("ARG")).build());

        when(countryService.getCountryWithMostBorders()).thenReturn(new CountryDTO("ARG", "Argentina"));

        this.mockMvc.perform(get("/api/countries/most-borders")).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ARG")).andExpect(jsonPath("$.name").value("Argentina"));

    }

    @Test
    public void postCountry() throws Exception {
        PostCountryDTO postCountryDTO = new PostCountryDTO();
        postCountryDTO.setAmountOfCountryToSave(1L);

        CountryDTO countryDTO = new CountryDTO("US", "United States");
        List<CountryDTO> countryDTOList = new ArrayList<>();
        countryDTOList.add(countryDTO);

        when(countryService.postCountries(any(PostCountryDTO.class))).thenReturn(countryDTOList);

        this.mockMvc.perform(post("/api/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amountOfCountryToSave\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("US"))
                .andExpect(jsonPath("$[0].name").value("United States"));
    }

}
