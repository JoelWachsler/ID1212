package id1212.wachsler.joel.rmi_and_databases.server.integration;

import javax.persistence.*;

/**
 * Data access object used to communicate with the database.
 * All calls to the database are encapsulated in this class.
 */
@Entity(name = "files")
public class FileDAO {
  private long id;
  private String name;
  private int size;
  private UserDAO owner;
  private boolean publicAccess = false;
  private boolean write = false;
  private boolean read = false;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(nullable = false)
  public int getSize() {
    return size;
  }

  @Column(nullable = false)
  public void setSize(int size) {
    this.size = size;
  }

  @Column(nullable = false)
  @ManyToOne
  public UserDAO getOwner() {
    return owner;
  }

  @Column(nullable = false)
  public void setOwner(UserDAO owner) {
    this.owner = owner;
  }

  @Column(nullable = false)
  public boolean isPublicAccess() {
    return publicAccess;
  }

  public void setPublicAccess(boolean publicAccess) {
    this.publicAccess = publicAccess;
  }

  @Column(nullable = false)
  public boolean isWrite() {
    return write;
  }

  public void setWrite(boolean write) {
    this.write = write;
  }

  @Column(nullable = false)
  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }
}
