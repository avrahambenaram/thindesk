package com.pfc.thindesk.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recursos")
public class Recurso {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private String tipo;
    private boolean disponivel;
}
