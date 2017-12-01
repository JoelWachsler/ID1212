package id1212.wachsler.joel.rmi_and_databases.common.dto;

import id1212.wachsler.joel.rmi_and_databases.server.integration.FileDAO;
import id1212.wachsler.joel.rmi_and_databases.server.model.File;

import java.io.Serializable;

public class FileDTO implements Serializable {
  private final long owner;
  private final String filename;
  private final long size;
  private final boolean publicAccess;
  private final boolean readable;
  private final boolean writable;

  public FileDTO(long owner, String filename, long size, boolean publicAccess, boolean readable, boolean writable) {
    this.owner = owner;
    this.filename = filename;
    this.size = size;
    this.publicAccess = publicAccess;
    this.readable = readable;
    this.writable = writable;
  }

  public FileDTO(File file) {
    this.owner = file.getOwner().getId();
    this.filename = file.getName();
    this.size = file.getSize();
    this.publicAccess = file.isPublicAccess();
    this.readable = file.isReadable();
    this.writable = file.isWritable();
  }

  public long getOwner() {
    return owner;
  }

  public String getFilename() {
    return filename;
  }

  public boolean isPublicAccess() {
    return publicAccess;
  }

  public boolean isReadable() {
    return readable;
  }

  public boolean isWritable() {
    return writable;
  }

  public long getSize() {
    return size;
  }
}
