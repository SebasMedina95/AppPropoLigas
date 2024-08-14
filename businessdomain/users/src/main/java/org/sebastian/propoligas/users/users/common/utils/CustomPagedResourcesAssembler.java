package org.sebastian.propoligas.users.users.common.utils;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

//* LA INTENCIÓN DE ESTA CLASE ES CENTRALIZAR LOS PROCESOS DE PAGINACIÓN QUE USAREMOS,
//* AUNQUE SPRING BOOT ES MUY AUTOMÁTICO, LA INTENCIÓN ES CENTRALIZAR EL PROCESO CON UNA PAGINACIÓN ESPECIALIZADA,
//* ADICIONAL, APLICANDO hateoas.PagedModel ES MEJOR CENTRALIZAR EL PROCESO YA QUE TENEMOS UNA PARTE DE PAGINACIÓN
//* ESTANDARIZADA CON EL JPA DE MANERA NORMAL Y OTRA CON EL PageModel QUE ES UNA RECOMENDACIÓN

@Component
public class    CustomPagedResourcesAssembler<T> {

    public PagedModel<T> toModel(Page<T> page, UriComponentsBuilder uriBuilder) {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                page.getSize(),
                (page.getNumber() + 1),  // Ajustar el número de página aquí
                page.getTotalElements(),
                page.getTotalPages()
        );

        PagedModel<T> resources = PagedModel.of(page.getContent(), metadata);

        if (page.hasPrevious()) {
            resources.add(createLinkWithPage(uriBuilder, page.getNumber(), page.getSize()).withRel("prev"));
        }
        resources.add(createLinkWithPage(uriBuilder, page.getNumber() + 1, page.getSize()).withSelfRel());
        if (page.hasNext()) {
            resources.add(createLinkWithPage(uriBuilder, page.getNumber() + 2, page.getSize()).withRel("next"));
        }

        resources.add(createLinkWithPage(uriBuilder, 1, page.getSize()).withRel("first"));
        resources.add(createLinkWithPage(uriBuilder, page.getTotalPages(), page.getSize()).withRel("last"));

        return resources;
    }

    private org.springframework.hateoas.Link createLinkWithPage(UriComponentsBuilder uriBuilder, int page, int size) {
        return org.springframework.hateoas.Link.of(
                uriBuilder
                        .replaceQueryParam("page", page)
                        .replaceQueryParam("size", size)
                        .toUriString()
        );
    }

}
