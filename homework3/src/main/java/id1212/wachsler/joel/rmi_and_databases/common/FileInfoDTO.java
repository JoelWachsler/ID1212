package id1212.wachsler.joel.rmi_and_databases.common;

import java.io.Serializable;

/**
 * Data transfer object for file.
 */
public class FileInfoDTO implements Serializable {
  private final String name;
  private final long size;
  private final String owner;
  private final boolean isPublic;
  private final boolean read;
  private final boolean write;

  public FileInfoDTO(String name, long size, String owner, boolean isPublic, boolean read, boolean write) {
    this.name = name;
    this.size = size;
    this.owner = owner;
    this.isPublic = isPublic;
    this.read = read;
    this.write = write;
  }

  @Override
  public String toString() {
    return String.format("File: %s, Size: %d, Owner: %s, Public: %s, Read: %s, Write: %s",
      name, size, owner, isPublic, read, write);
  }
}
