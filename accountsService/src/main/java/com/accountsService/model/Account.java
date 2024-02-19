package com.accountsService.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@XmlRootElement
@Schema(name = "Modelo de cuenta", description = "Representa una cuenta de un cliente")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(1)
    @Schema(name = "Id cuenta", description = "Identificador de la cuenta basado en números enteros positivos", example = "12")
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Schema(name = "Tipo de cuenta", description = "Tipo de la cuenta en caracters alfanuméricos", example = "Tipo1")
    private String type;

    @DateTimeFormat
    @Schema(name = "Fecha apertura cuenta", description = "Fecha y hora en la que se ha abierto la cuenta", example = "2024-02-16T12:37:56.401+00:00")
    Date openingDate;

    private int balance;

    @NotNull
    private Long ownerId;

    @Transient
    Customer owner;


}
