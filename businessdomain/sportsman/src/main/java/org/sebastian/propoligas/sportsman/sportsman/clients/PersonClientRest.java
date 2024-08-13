package org.sebastian.propoligas.sportsman.sportsman.clients;

import org.sebastian.propoligas.sportsman.sportsman.common.utils.ApiResponseConsolidation;
import org.sebastian.propoligas.sportsman.sportsman.models.Persons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//! NOTA -> El microservicio se encuentra dockerizado
//? Ojo, name tiene que ser spring.application.name del micro servicio a relacionar.
//? Ojo, url La URL es el LocalHost hasta antes de la definición del método.

//? Nota1, el puerto es 13551 en 100% local, pero, al dockerizar queda siendo 22335
//? Nota2, el puerto es 22335 como requerimos una consulta interna usamos el puerto interno, es decir, 8080
//? Nota3, para no tener que crear una red interna, apuntemos al contenedor de docker en vez de localhost
@FeignClient(name = "persons", url = "persons-app-ms:8080/business/v1/api/persons")
public interface PersonClientRest {

    @GetMapping("/find-by-id/{id}")
    ApiResponseConsolidation<Persons> getPerson(@PathVariable("id") Long id);

    //! Funcionalidad especial para la búsqueda por filtros
    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<Long>> findPersonIdsByCriteria(@RequestParam("search") String search);

}
