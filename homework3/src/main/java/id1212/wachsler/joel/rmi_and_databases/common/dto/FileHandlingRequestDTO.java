package id1212.wachsler.joel.rmi_and_databases.common.dto;

import id1212.wachsler.joel.rmi_and_databases.common.Header;

import java.io.Serializable;

public class FileHandlingRequestDTO implements Serializable {
  private final Header upload;
  private final String fileUploadName;
  private final long userId;

  public FileHandlingRequestDTO(Header upload, String fileUploadName, long userId) {
    this.upload = upload;
    this.fileUploadName = fileUploadName;
    this.userId = userId;
  }

  public Header getUpload() {
    return upload;
  }

  public String getFileUploadName() {
    return fileUploadName;
  }

  public long getUserId() {
    return userId;
  }
}
