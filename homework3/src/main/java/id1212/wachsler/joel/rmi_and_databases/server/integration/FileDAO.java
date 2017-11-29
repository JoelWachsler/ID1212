package id1212.wachsler.joel.rmi_and_databases.server.integration;

import javax.persistence.*;

/**
 * Data access object used to communicate with the database.
 */
@Entity(name = "File")
public class FileDAO extends HibernateSession {
  private long id;
  private String name;
  private int size;
  private UserDAO owner;
  private boolean publicAccess = false;
  private boolean writable = false;
  private boolean readable = false;

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(unique = true, nullable = false)
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

  public boolean isPublicAccess() {
    return publicAccess;
  }

  public void setPublicAccess(boolean publicAccess) {
    this.publicAccess = publicAccess;
  }

  public boolean isWritable() {
    return writable;
  }

  public void setWritable(boolean write) {
    this.writable = write;
  }

  public boolean isReadable() {
    return readable;
  }

  public void setReadable(boolean read) {
    this.readable = read;
  }

  @ManyToOne(optional = false)
  public UserDAO getOwner() {
    return owner;
  }

  public void setOwner(UserDAO owner) {
    this.owner = owner;
  }
}
