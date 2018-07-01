package org.sitmun.plugin.core.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitmun.plugin.core.domain.Cartography;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartographyRepositoryTest {

    @Autowired
    private CartographyRepository cartographyRepository;

    private Cartography cartography;

    /**
     * 
     */
    @Before
    public void init() {
        cartography = new Cartography();
        cartography.setName("Test");
        cartography.setLayers(null);
        cartography.setSelectionLayer(null);
        cartography.setConnection(null);
        cartography.setAvailabilities(null);
        cartography.setEditable(true);
        cartography.setMaximumScale(null);
        cartography.setMinimumScale(null);
        cartography.setCreatedDate(new Date());
        cartography.setOrder(0);
        cartography.setQueryable(true);
        cartography.setQueryAct(true);
        cartography.setQueryLay(true);
        cartography.setSelectable(true);
        cartography.setService(null);
        cartography.setSelectionService(null);
        cartography.setThemeable(true);
        cartography.setLegendTip(null);
        cartography.setType(null);
        cartography.setGeometryType(null);
        cartography.setTransparency(0);
        cartography.setLegendUrl(null);
        cartography.setMetadataUrl(null);
        cartography.setVisible(true);

    }

    @Test
    public void saveCartography() {
        assumeThat(cartographyRepository.findOne(cartography.getId())).isNull();
        cartographyRepository.save(cartography);
        assertThat(cartography.getId()).isNotZero();
    }

    @Test
    public void findOneCartographyById() {
        assumeThat(cartographyRepository.findOne(cartography.getId())).isNull();
        cartographyRepository.save(cartography);
        assumeThat(cartography.getId()).isNotZero();

        assertThat(cartographyRepository.findOne(cartography.getId())).isNotNull();
    }
}

