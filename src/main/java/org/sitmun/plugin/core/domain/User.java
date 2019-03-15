package org.sitmun.plugin.core.domain;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STM_USUARIO", uniqueConstraints = {@UniqueConstraint(name = "STM_USU_USU_UK", columnNames = {"USU_USUARIO"})})
public class User implements Identifiable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STM_GENERATOR")
  @SequenceGenerator(name = "STM_GENERATOR", sequenceName = "STM_SEQ")
  @Column(name = "USU_CODIGO")
  private long id;

  @NotNull
  @Column(name = "USU_USUARIO", nullable = false, length = 30)
  private String username;

  @Column(name = "USU_PASSWORD", length = 128)
  private String password;

  @Column(name = "USU_NOMBRE", length = 30)
  private String firstName;

  @Column(name = "USU_APELLIDOS", length = 40)
  private String lastName;

  @Column(name = "USU_ADM")
  private Boolean administrator;

  @Column(name = "USU_BLOQ")
  private Boolean blocked;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserPosition> positions = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserConfiguration> permissions = new HashSet<>();

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getAdministrator() {
    return administrator;
  }

  public void setAdministrator(Boolean administrator) {
    this.administrator = administrator;
  }

  public Boolean getBlocked() {
    return blocked;
  }

  public void setBlocked(Boolean blocked) {
    this.blocked = blocked;
  }

  public Set<UserPosition> getPositions() {
    return positions;
  }

  public void setPositions(Set<UserPosition> positions) {
    this.positions = positions;
  }

  public Set<UserConfiguration> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<UserConfiguration> permissions) {
    this.permissions = permissions;
  }

}
