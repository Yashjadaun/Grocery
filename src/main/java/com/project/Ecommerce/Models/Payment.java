package com.project.Ecommerce.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Order orders;

    @NotBlank
    @Size(min=3,message = "Payment method must contian at least 4 characters ")
    private String PaymentMethod;

    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(String PaymentMethod,String pgPaymentId,String pgStatus,String pgResponseMessage,String pgName ){
          this.PaymentMethod=PaymentMethod;
          this.pgPaymentId=pgPaymentId;
          this.pgStatus=pgStatus;
          this.pgResponseMessage=pgResponseMessage;
          this.pgName=pgName;
    }

}
