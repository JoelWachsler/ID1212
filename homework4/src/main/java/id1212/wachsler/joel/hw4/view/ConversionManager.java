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
  private CurrencyFacade currencyController;

  public List<? extends CurrencyDTO> getCurrencies() {
    return currencyController.getCurrencies();
  }
}
