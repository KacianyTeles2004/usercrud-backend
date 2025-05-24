package com.example.usercrud.model;

import com.example.usercrud.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FormaPagamento forma;

    // Cartão
    private String numeroCartao;
    private String nomeCompleto;
    private String validade;
    private String codigoSeguranca;
    private Integer parcelas;
}
