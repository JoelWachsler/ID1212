package id1212.wachsler.joel.hw4.integration;

import id1212.wachsler.joel.hw4.model.Currency;
import id1212.wachsler.joel.hw4.model.Rate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class RateDAO {
  @PersistenceContext(unitName = "HibernatePU")
  private EntityManager em;

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
