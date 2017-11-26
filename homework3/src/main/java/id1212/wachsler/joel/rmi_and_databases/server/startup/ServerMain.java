package id1212.wachsler.joel.rmi_and_databases.server.startup;

import id1212.wachsler.joel.rmi_and_databases.common.dto.CredentialDTO;
import id1212.wachsler.joel.rmi_and_databases.common.FileServer;
import id1212.wachsler.joel.rmi_and_databases.server.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class ServerMain {
  private final String datasource = "file_server";
  private final CredentialDTO dbLogin = new CredentialDTO("root", "");

  public static void main(String[] args) {
    try {
      ServerMain server = new ServerMain();
      server.startRMIServant();
      System.out.println("Server started.");
    } catch (Exception e) {
      System.err.println("Failed to start server");
      e.printStackTrace();
    }
  }

  private void startRMIServant() throws RemoteException, MalformedURLException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
    try {
      LocateRegistry.getRegistry().list();
    } catch (RemoteException e) {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    Controller controller = new Controller(datasource, dbLogin);
    Naming.rebind(FileServer.REGISTRY_NAME ,controller);
  }
}
