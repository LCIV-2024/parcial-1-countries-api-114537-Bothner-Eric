package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.PostCountryDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/api/countries")
    public List<CountryDTO> getAllCountries(@RequestParam(required = false) String name, @RequestParam(required = false) String code) {
        if (name != null) {
            return countryService.getAllCountriesDTO().stream().filter(country -> country.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if (code != null) {
            return countryService.getAllCountriesDTO().stream().filter(country -> country.getCode().toLowerCase().contains(code.toLowerCase())).collect(Collectors.toList());
        }
        return countryService.getAllCountriesDTO();
    }

    @GetMapping("/api/countries/{continent}/continent")
    public List<CountryDTO> getCountriesByContinent(@PathVariable String continent) {
        return countryService.getCountriesByContinent(continent);
    }


    @GetMapping("/api/countries/{language}/language")
    public List<CountryDTO> getCountriesByLanguage(@PathVariable String language) {
        return countryService.getCountriesByLanguage(language);
    }

    @GetMapping("/api/countries/most-borders")
    public CountryDTO getCountryWithMostBorders() {
        return countryService.getCountryWithMostBorders();
    }

    @PostMapping("/api/countries")
    public List<CountryDTO> addCountry(@RequestBody PostCountryDTO postCountryDTO) {
        return countryService.postCountries(postCountryDTO);
    }

}