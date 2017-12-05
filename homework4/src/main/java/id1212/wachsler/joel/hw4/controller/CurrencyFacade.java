package id1212.wachsler.joel.hw4.controller;

import id1212.wachsler.joel.hw4.integration.CurrencyDAO;
import id1212.wachsler.joel.hw4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@Stateless
public class CurrencyFacade {
  @EJB
  CurrencyDAO currencyDAO;

  public List<? extends CurrencyDTO> getCurrencies() {
    return currencyDAO.getCurrencies();
  }
}
