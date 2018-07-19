package org.sitmun.plugin.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "stm_usuconf", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ucf_codusu", "ucf_codter", "ucf_codrol"})
})
public class UserConfiguration {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ucf_codigo")
  private long id;

  @JoinColumn(name = "ucf_codusu")
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne
  //@MapsId("territorioId")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "ucf_codter")
  private Territory territory;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  //@MapsId("rolId")
  @JoinColumn(name = "ucf_codrol")
  private Role role;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Territory getTerritory() {
    return territory;
  }

  public void setTerritory(Territory territory) {
    this.territory = territory;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }


}