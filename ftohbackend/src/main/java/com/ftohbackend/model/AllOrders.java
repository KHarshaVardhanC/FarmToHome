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

   @ManyToOne
   @JoinColumn(name = "product_id", nullable = false)
    private AllProduct product;
=======
  
>>>>>>> 549a94784080db3c4d4e72bb93c8520cfdc3c5bf

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private allProductModel product;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

  //  private double productPrice;
    //private double productQuantity;
}
