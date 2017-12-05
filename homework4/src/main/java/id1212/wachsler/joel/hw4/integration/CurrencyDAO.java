package id1212.wachsler.joel.hw4.integration;

import id1212.wachsler.joel.hw4.model.Currency;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CurrencyDAO {
  @PersistenceContext(unitName = "HibernatePU")
  private EntityManager em;

  public List<Currency> getCurrencies() {
    return em.createNamedQuery("getAllCurrencies", Currency.class).getResultList();
  }
}
