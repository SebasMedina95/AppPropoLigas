package org.sebastian.propoligas.sportsman.sportsman.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TBL_PERSONS_SPORTSMAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonsSportsManRelation {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    private Long id;

    /* En esta relación solo puede estar una vez el usuario a nivel de deportista */
    @Column(name = "PERSON_ID", unique = true, nullable = false)
    private Long personId;

}
