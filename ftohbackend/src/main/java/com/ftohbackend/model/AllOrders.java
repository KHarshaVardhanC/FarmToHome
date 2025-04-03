package com.ftohbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AllOrders")
public class AllOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

<<<<<<< HEAD
  //  @ManyToOne
   // @JoinColumn(name = "product_id", nullable = false)
    //private AllProduct product;
=======
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private allProductModel product;
>>>>>>> bea7448b35aac8b86ebf9a3016de2bf4aa66a71b

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

  //  private double productPrice;
    //private double productQuantity;
}
