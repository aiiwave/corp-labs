package kz.narxoz.carrental.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import kz.narxoz.carrental.config.Application;
import kz.narxoz.carrental.persistence.RentalJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/** Whole stack on real PostgreSQL: the second POST of a key is 409 and the count stays 1. */
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class RentalApiIntegrationTest {

    private static final String BASE = "/api/v1/rental-requests";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RentalJpa rentals;
    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("DELETE FROM rental_request");
    }

    private static String json(String key, String title) {
        return "{\"businessKey\":\"%s\",\"title\":\"%s\"}".formatted(key, title);
    }

    @Test
    void createThenReadByLocation() throws Exception {
        MvcResult created = mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON)
                        .content(json("RR-301", "Toyota Camry, 3 days")))
                .andExpect(status().isCreated())
                .andReturn();
        String location = created.getResponse().getHeader("Location");
        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessKey").value("RR-301"))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
        assertEquals(1, rentals.count("RR-301"));
    }

    @Test
    void secondPostOfSameKeyIs409AndCountStaysOne() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-302", "First")))
                .andExpect(status().isCreated());
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-302", "Second")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE"));
        assertEquals(1, rentals.count("RR-302"));
    }

    @Test
    void unknownUuidIs404() throws Exception {
        mvc.perform(get(BASE + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void blankTitleLeavesNoRow() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-303", " ")))
                .andExpect(status().isBadRequest());
        assertEquals(0, rentals.count("RR-303"));
    }

    @Test
    void openApiListsStatuses() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/rental-requests'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/rental-requests'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/rental-requests'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/rental-requests/{id}'].get.responses['200']").exists())
                .andExpect(jsonPath("$.paths['/api/v1/rental-requests/{id}'].get.responses['404']").exists());
    }
}
