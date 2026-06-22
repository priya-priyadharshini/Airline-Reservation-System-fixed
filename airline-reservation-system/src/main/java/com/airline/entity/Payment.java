package com.airline.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Entity
public class Payment{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String method; private String maskedReference; private BigDecimal amount; @Enumerated(EnumType.STRING) private PaymentStatus status=PaymentStatus.SUCCESS; private LocalDateTime paidAt=LocalDateTime.now();
 @OneToOne private Booking booking;
 public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getMethod(){return method;} public void setMethod(String method){this.method=method;} public String getMaskedReference(){return maskedReference;} public void setMaskedReference(String maskedReference){this.maskedReference=maskedReference;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal amount){this.amount=amount;} public PaymentStatus getStatus(){return status;} public void setStatus(PaymentStatus status){this.status=status;} public LocalDateTime getPaidAt(){return paidAt;} public void setPaidAt(LocalDateTime paidAt){this.paidAt=paidAt;} public Booking getBooking(){return booking;} public void setBooking(Booking booking){this.booking=booking;}
}
