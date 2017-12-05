package id1212.wachsler.joel.hw4.view;

import id1212.wachsler.joel.hw4.model.AdderBean;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adder")
@SessionScoped
public class Adder implements Serializable {
  @EJB
  private AdderBean adderBean;
  private int numberToAdd;

  public int getTotal() {
    return adderBean.getTotal();
  }

  public void add() {
    adderBean.add(numberToAdd);
  }

  public void setNumberToAdd(Integer numberToAdd) {
    this.numberToAdd = numberToAdd;
  }

  public Integer getNumberToAdd() {
    return null;
  }
}
