package org.sitmun.plugin.core.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitmun.plugin.core.domain.Cartography;
import org.sitmun.plugin.core.domain.Territory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CartographyRestResourceIntTest {

    @Autowired
    private MockMvc mvc;

    private Cartography cartography;
    
    private Territory territory;

    private static final String TERRITORY_NAME = "Territory Name";
    private static final String CARTOGRAPHY_NAME = "Cartographt Name";
    private static final String NEW_TERRITORY_URI = "http://localhost/api/territories/1";
    private static final String NEW_CARTOGRAPHY_URI = "http://localhost/api/cartographies/1";

    @Before
    public void init() {
        territory = new Territory();
        territory.setBlocked(false);
        territory.setName(TERRITORY_NAME);

        cartography = new Cartography();
        cartography.setName(CARTOGRAPHY_NAME);
    }

    @Test
    public void postTerritory() throws Exception {

        mvc.perform(post("/api/territories")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(Util.convertObjectToJsonBytes(territory))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(NEW_TERRITORY_URI)));

        mvc.perform(get("/api/territories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Util.APPLICATION_HAL_JSON_UTF8))
                .andExpect(jsonPath("$.name", equalTo(TERRITORY_NAME)));

    }

    @Test
    public void postCartograpy() throws Exception {

        mvc.perform(post("/api/cartographies")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(Util.convertObjectToJsonBytes(cartography))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(NEW_CARTOGRAPHY_URI)));

        mvc.perform(get("/api/cartographies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(Util.APPLICATION_HAL_JSON_UTF8))
                .andExpect(jsonPath("$.name", equalTo(CARTOGRAPHY_NAME)));
    }
}

