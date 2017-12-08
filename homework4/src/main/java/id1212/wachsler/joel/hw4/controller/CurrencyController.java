package id1212.wachsler.joel.hw4.controller;

import id1212.wachsler.joel.hw4.integration.CurrencyDAO;
import id1212.wachsler.joel.hw4.integration.RateDAO;
import id1212.wachsler.joel.hw4.model.Currency;
import id1212.wachsler.joel.hw4.model.CurrencyDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Handles communication with the currency models.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class CurrencyController {
  @EJB CurrencyDAO currencyDAO;
  @EJB RateDAO rateDAO;

  public List<? extends CurrencyDTO> getCurrencies() {
    return currencyDAO.getCurrencies();
  }

  /**
   * Converts <code>amntToConvert</code> which is of the currency type corresponding to <code>fromId</code> to
   * the currency corresponding to <code>toId</code>.
   *
   * @param fromId The id of the currency <code>amntToConvert</code> is in.
   * @param toId The id of the currency to convert to.
   * @param amntToConvert The amount to convert.
   * @return The converted amount.
   */
  public float convert(long fromId, long toId, float amntToConvert) {
    Currency from = currencyDAO.findCurrencyById(fromId);
    Currency to = currencyDAO.findCurrencyById(toId);

    return rateDAO.getRate(from, to).getRate() * amntToConvert;
  }
}
