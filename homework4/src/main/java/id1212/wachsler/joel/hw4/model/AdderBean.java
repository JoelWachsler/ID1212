package id1212.wachsler.joel.hw4.model;

import javax.ejb.Stateful;

@Stateful
public class AdderBean {
  private int total;

  public void add(int numberToAdd) {
    total += numberToAdd;
  }

  public int getTotal() {
    return total;
  }
}
