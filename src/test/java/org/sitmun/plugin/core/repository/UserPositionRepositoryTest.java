package org.sitmun.plugin.core.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitmun.plugin.core.domain.Territory;
import org.sitmun.plugin.core.domain.User;
import org.sitmun.plugin.core.domain.UserPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserPositionRepositoryTest {
    
    @Autowired
    private UserPositionRepository userPositionRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TerritoryRepository territorioRepository;
 
    private UserPosition userPosition;


    @Before
    public void init() {

        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setAdministrator(true);
        user.setBlocked(false);
        user.setPassword("prCTmrOYKHQ=");
        user.setUsername("admin");
        user.setPositions(null);
        user.setPermissions(null);
        userRepository.save(user);

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
        territorioRepository.save(territory);
        
        userPosition = new UserPosition();
        userPosition.setName("Test");
        userPosition.setEmail(null);
        userPosition.setCreatedDate(new Date());
        userPosition.setDatedDate(null);
        userPosition.setOrganization("Test");
        userPosition.setTerritory(territory);
        userPosition.setUser(user);

    }

    @Test
    public void saveUserPosition() {
        assumeThat(userPositionRepository.findOne(userPosition.getId())).isNull();
        userPositionRepository.save(userPosition);
        assertThat(userPosition.getId()).isNotZero();
    }

    @Test
    public void findOneUserPositionById() {
        assumeThat(userPositionRepository.findOne(userPosition.getId())).isNull();
        userPositionRepository.save(userPosition);
        assumeThat(userPosition.getId()).isNotZero();

        assertThat(userPositionRepository.findOne(userPosition.getId())).isNotNull();
    }
}
