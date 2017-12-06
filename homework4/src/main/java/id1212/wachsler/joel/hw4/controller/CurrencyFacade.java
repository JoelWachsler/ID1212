package id1212.wachsler.joel.hw4.controller;

import id1212.wachsler.joel.hw4.integration.CurrencyDAO;
import id1212.wachsler.joel.hw4.integration.RateDAO;
import id1212.wachsler.joel.hw4.model.Currency;
import id1212.wachsler.joel.hw4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class CurrencyFacade {
  @EJB
  CurrencyDAO currencyDAO;
  @EJB
  RateDAO rateDAO;

  public List<? extends CurrencyDTO> getCurrencies() {
    return currencyDAO.getCurrencies();
  }

  public float convert(long fromId, long toId, float amntToConvert) {
    Currency from = currencyDAO.findCurrencyById(fromId);
    Currency to = currencyDAO.findCurrencyById(toId);

    return rateDAO.getRate(from, to).getRate() * amntToConvert;
  }
}
