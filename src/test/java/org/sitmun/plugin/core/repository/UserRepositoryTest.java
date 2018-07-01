package org.sitmun.plugin.core.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitmun.plugin.core.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
      
    private User user;

    @Before
    public void init() {
        TerritoryType type = new TerritoryType();
        type.setId(1);
        type.setName("tipo Territorio 1");

        Territory territory = new Territory();
        territory.setName("Admin");
        territory.setScope(null);
        territory.setBlocked(false);
        territory.setAddress(null);
        territory.setEmail("email@email.org");
        territory.setExt(null);
        territory.setCreatedDate(new Date());
        territory.setLogo(null);
        territory.setMembers(null);
        territory.setOrganizationName("Test");
        territory.setComments(null);
        territory.setType(type);

        user = new User();
        user.setFirstName("Admin");
        user.setLastName("AdminLastName");
        user.setAdministrator(true);
        user.setBlocked(false);
        user.setPassword("prCTmrOYKHQ=");
        user.setUsername("admin");
        user.setPositions(null);
        user.setPermissions(null);
        
        Role role = new Role();
        role.setId(0);
        role.setName("rol-admin");
        role.setComments("rol de administrador");
        UserConfiguration conf = new UserConfiguration();
        conf.setId(1);
        conf.setUser(user);
        conf.setRole(role);
        conf.setTerritory(territory);

    }

    @Test
    public void saveUser() {
        assumeThat(userRepository.findOne(user.getId())).isNull();
        userRepository.save(user);
        assertThat(user.getId()).isNotZero();
    }

    @Test
    public void findOneuserById() {
        assumeThat(userRepository.findOne(user.getId())).isNull();
        userRepository.save(user);
        assumeThat(user.getId()).isNotZero();

        assertThat(userRepository.findOne(user.getId())).isNotNull();
    }

}