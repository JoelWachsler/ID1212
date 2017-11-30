package id1212.wachsler.joel.rmi_and_databases.common.dto;

import java.io.Serializable;

public class FileDTO implements Serializable {
  private final long owner;
  private final String filename;
  private final boolean publicAccess;
  private final boolean readable;
  private final boolean writable;

  public FileDTO(long owner, String filename, boolean publicAccess, boolean readable, boolean writable) {
    this.owner = owner;
    this.filename = filename;
    this.publicAccess = publicAccess;
    this.readable = readable;
    this.writable = writable;
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
}
