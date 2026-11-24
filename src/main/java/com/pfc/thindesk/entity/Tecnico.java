package com.pfc.thindesk.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tecnicos")
public class Tecnico {
    @Id
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String especialidade;
    private boolean ativo;
}
