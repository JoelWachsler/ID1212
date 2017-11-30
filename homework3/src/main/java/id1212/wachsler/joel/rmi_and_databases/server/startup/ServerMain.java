package id1212.wachsler.joel.rmi_and_databases.server.startup;

import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.server.controller.Controller;
import id1212.wachsler.joel.rmi_and_databases.server.model.HibernateSession;
import id1212.wachsler.joel.rmi_and_databases.server.net.Listener;

import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stars the file handling server.
 */
public class ServerMain {
  public static void main(String[] args) throws BindException {
    // Set logging to a more reasonable level
    Logger log = Logger.getLogger("org.hibernate");
    log.setLevel(Level.SEVERE);

    try {
      HibernateSession.initSessionFactory();
      System.out.println("Hibernate started.");

      ServerMain server = new ServerMain();
      Controller controller = new Controller();

      server.startRMIServant(controller);
      System.out.println("RMI server started.");

      server.startFileServerListener(controller);
    } catch (Exception e) {
      System.err.println("Failed to start server");
      e.printStackTrace();
    }
  }

  private void startFileServerListener(Controller controller) {
    new Listener(controller);
  }

  private void startRMIServant(Controller controller) throws RemoteException, MalformedURLException {
    try {
      LocateRegistry.getRegistry().list();
    } catch (RemoteException e) {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    Naming.rebind(FileServer.REGISTRY_NAME, controller);
  }
}
