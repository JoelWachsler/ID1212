package id1212.wachsler.joel.hw4.view;

import id1212.wachsler.joel.hw4.controller.CurrencyFacade;
import id1212.wachsler.joel.hw4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("conversionManager")
@ConversationScoped
public class ConversionManager implements Serializable {
  @EJB
  private CurrencyFacade currencyFacade;
  private float amntToConvert = 0;
  private float amntConverted = 0;
  private long fromId = 1;
  private long toId = 1;

  public List<? extends CurrencyDTO> getCurrencies() {
    return currencyFacade.getCurrencies();
  }

  public void convert() {
    amntConverted = currencyFacade.convert(fromId, toId, amntToConvert);
  }

  public CurrencyFacade getCurrencyFacade() {
    return currencyFacade;
  }

  public void setCurrencyFacade(CurrencyFacade currencyFacade) {
    this.currencyFacade = currencyFacade;
  }

  public float getAmntToConvert() {
    return amntToConvert;
  }

  public void setAmntToConvert(float amntToConvert) {
    this.amntToConvert = amntToConvert;
  }

  public float getAmntConverted() {
    return amntConverted;
  }

  public void setAmntConverted(float amntConverted) {
    this.amntConverted = amntConverted;
  }

  public long getFromId() {
    return fromId;
  }

  public void setFromId(long fromId) {
    this.fromId = fromId;
  }

  public long getToId() {
    return toId;
  }

  public void setToId(long toId) {
    this.toId = toId;
  }
}
