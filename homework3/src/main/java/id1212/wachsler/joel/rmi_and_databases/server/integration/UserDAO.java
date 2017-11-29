package id1212.wachsler.joel.rmi_and_databases.server.integration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data access object used to communicate with the database.
 */
@Entity(name = "User")
public class UserDAO extends HibernateSession {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(unique = true, nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;
  @OneToMany(mappedBy = "owner")
  private Collection<FileDAO> files = new ArrayList<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<FileDAO> getFiles() {
    return files;
  }

  public void setFiles(Collection<FileDAO> files) {
    this.files = files;
  }
}
