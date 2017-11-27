package id1212.wachsler.joel.rmi_and_databases.server.startup;

import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.server.controller.Controller;
import id1212.wachsler.joel.rmi_and_databases.server.integration.DB;
import id1212.wachsler.joel.rmi_and_databases.server.net.fileTransfer.Listener;

import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

/**
 * Stars the file handling server.
 */
public class ServerMain {
  private static final String datasource = "file_server";
  private static final CredentialDTO dbLogin = new CredentialDTO("root", "");

  public static void main(String[] args) throws BindException {
    try {
      ServerMain server = new ServerMain();
      server.initDB(datasource, dbLogin);

      Controller controller = new Controller();

      server.startRMIServant(controller);
      System.out.println("RMI server started.");

      server.startFileServerListener(controller);
    } catch (Exception e) {
      System.err.println("Failed to start server");
      e.printStackTrace();
    }
  }

  private void initDB(String datasource, CredentialDTO dbLogin) throws SQLException {
    DB.init(datasource, dbLogin);
    System.out.println("Database started!");
  }

  private void startFileServerListener(Controller controller) {
    new Listener(controller);
  }

  private void startRMIServant(Controller controller) throws RemoteException, MalformedURLException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    try {
      LocateRegistry.getRegistry().list();
    } catch (RemoteException e) {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    Naming.rebind(FileServer.REGISTRY_NAME, controller);
  }
}
