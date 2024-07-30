package org.sebastian.propoligas.sportsman.sportsman.clients;

import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponse;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//? Ojo, name tiene que ser spring.application.name del micro servicio a relacionar.
//? Ojo, url La URL es el LocalHost hasta antes de la definición del método.
@FeignClient(name = "persons", url = "localhost:13551/api/persons")
public interface PersonClientRest {

    @GetMapping("/find-by-id/{id}")
    ApiResponse<Persons> getPerson(@PathVariable("id") Long id);

}
