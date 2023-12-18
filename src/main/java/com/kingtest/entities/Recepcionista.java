package com.kingtest.entities;

import com.kingtest.dto.responses.RecepcionistaSafeDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recepcionistas")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Recepcionista {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // @Column(name = "primeiro_nome")
    // private String firstName;

    // @Column(name = "sobrenome")
    // private String lastName;

    @Column(name="nome_completo")
    private String fullName;

    @Column(name = "data_nascimento")
    private String birthday;

    @Column(name = "email")
    private String email;

    @Column(name = "clinica_id")
    private long clinicaId;

    @Column(name = "user_id")
    private long userId;

    public Recepcionista(String fullName, String birthday, String email, long clinicaId,
            Long userId) {
        // this.firstName = firstName;
        // this.lastName = lastName;
        this.fullName = fullName;
        this.birthday = birthday;
        this.email = email;
        this.clinicaId = clinicaId;
        this.userId = userId;
    }

    public RecepcionistaSafeDto toSafeDTO() {
        return RecepcionistaSafeDto.builder()
            .id(id)
            // .firstName(firstName)
            // .lastName(lastName)
            .fullName(fullName)
            .birthday(birthday)
            .email(email)
            .clinicId(clinicaId)
            .userId(userId)
            .build();
    }
}
