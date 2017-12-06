package id1212.wachsler.joel.hw4.integration;

import id1212.wachsler.joel.hw4.model.Currency;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CurrencyDAO {
  @PersistenceContext(unitName = "HibernatePU")
  private EntityManager em;

  public List<Currency> getCurrencies() {
    return em.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
  }

  public Currency findCurrencyById(long fromId) {
    Currency currency = em.find(Currency.class, fromId);

    if (currency == null)
      throw new EntityNotFoundException(String.format("No currency with the id \"%d\" was found!", fromId));

    return currency;
  }
}
