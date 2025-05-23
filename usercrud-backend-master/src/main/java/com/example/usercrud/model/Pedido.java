package com.example.usercrud.model;

import com.example.usercrud.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPedido;

    private LocalDateTime dataPedido;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Endereco enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal valorTotal;

    @OneToOne(cascade = CascadeType.ALL)
    private Pagamento pagamento;


    public Endereco getEndereco() {
        return this.enderecoEntrega;
    }
}
