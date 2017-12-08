package id1212.wachsler.joel.hw4.integration;

import id1212.wachsler.joel.hw4.model.Currency;
import id1212.wachsler.joel.hw4.model.Rate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Rate Data Access Object.
 * Handles various data access operations regarding rate.
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class RateDAO {
  @PersistenceContext(unitName = "HibernatePU")
  private EntityManager em;

  /**
   * Finds the conversion rate between
   *
   * @param from Rate from this currency.
   * @param to Rate to this currency.
   * @return The rate between the two currencies.
   */
  public Rate getRate(Currency from, Currency to) {
    try {
      return em.createNamedQuery("getRateFromCurrencies", Rate.class)
        .setParameter("fromCurr", from)
        .setParameter("toCurr", to)
        .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
