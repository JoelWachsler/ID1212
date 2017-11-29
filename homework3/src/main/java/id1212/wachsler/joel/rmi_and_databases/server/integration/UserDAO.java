package id1212.wachsler.joel.rmi_and_databases.server.integration;

import javax.persistence.*;
import java.util.Set;

/**
 * Data access object used to communicate with the database.
 * All calls to the database are encapsulated in this class.
 */
@Entity(name = "users")
public class UserDAO {

  private long id;
  private String username;
  private String password;
  private Set<FileDAO> files;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  @Column(unique = true, nullable = false)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(nullable = false)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @OneToMany
  public Set<FileDAO> getFiles() {
    return files;
  }

  public void setFiles(Set<FileDAO> files) {
    this.files = files;
  }
}
