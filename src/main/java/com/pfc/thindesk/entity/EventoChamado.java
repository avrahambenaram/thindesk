package com.pfc.thindesk.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Uma entrada do histórico do chamado (abertura, resolução...). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoChamado {

    private String acao;        // ABERTURA ou RESOLUCAO
    private String responsavel; // usuário que abriu, ou técnico que resolveu
    private String motivo;      // texto informado (descrição na abertura, motivo na resolução)
    private LocalDateTime data;
}
