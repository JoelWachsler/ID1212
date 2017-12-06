package id1212.wachsler.joel.hw4.model;

public interface RateDTO {
  Currency getFrom();
  void setFrom(Currency from);
  Currency getTo();
  void setTo(Currency to);
  float getRate();
  void setRate(float rate);
}
