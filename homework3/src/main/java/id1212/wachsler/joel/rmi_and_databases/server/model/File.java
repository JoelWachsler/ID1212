package id1212.wachsler.joel.rmi_and_databases.server.model;

import id1212.wachsler.joel.rmi_and_databases.server.integration.HibernateSession;

import javax.persistence.*;

/**
 * Data access object used to communicate with the database.
 */
@Entity(name = "File")
public class File extends HibernateSession {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(unique = true, nullable = false)
  private String name;
  @Column(nullable = false)
  private long size;
  @ManyToOne(optional = false)
  private User owner;
  @Column(nullable = false)
  private boolean publicAccess = false;
  @Column(nullable = false)
  private boolean writable = false;
  @Column(nullable = false)
  private boolean readable = false;

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
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

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }
}
