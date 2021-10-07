package com.example.demo.core.stock.entity.shoe;

import com.example.demo.dto.in.ShoeFilter.Color;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shoe")
public class ShoeEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private BigInteger size;
  @Enumerated(EnumType.STRING)
  private Color color;
  private BigInteger quantity;

  public ShoeEntity() {
  }

  public ShoeEntity(BigInteger size, Color color) {
    setSize(size);
    setColor(color);
  }

  public ShoeEntity(BigInteger size, Color color, BigInteger quantity) {
    setSize(size);
    setColor(color);
    setQuantity(quantity);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigInteger getQuantity() {
    return quantity;
  }

  public void setQuantity(BigInteger quantity) {
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigInteger getSize() {
    return size;
  }

  public void setSize(BigInteger size) {
    this.size = size;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}