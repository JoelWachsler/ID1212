package id1212.wachsler.joel.rmi_and_databases.common.dto;

import id1212.wachsler.joel.rmi_and_databases.common.Header;

import java.io.Serializable;

public class FileHandlingResponseDTO implements Serializable {
  private Header header;

  public FileHandlingResponseDTO(Header header) {
    this.header = header;
  }

  public Header getHeader() {
    return header;
  }
}
