package kz.narxoz.carrental.handler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import kz.narxoz.carrental.config.Application;
import kz.narxoz.carrental.config.RentalQueryService;
import kz.narxoz.carrental.config.RentalRegistrationService;
import kz.narxoz.carrental.domain.DuplicateRental;
import kz.narxoz.carrental.domain.Rental;
import kz.narxoz.carrental.domain.RentalId;
import kz.narxoz.carrental.domain.RentalNotFound;
import kz.narxoz.carrental.domain.RentalStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/** Web slice: no PostgreSQL, no real HTTP server. Services are mocked. */
@WebMvcTest(RentalController.class)
@ContextConfiguration(classes = Application.class)
class RentalControllerTest {

    private static final String BASE = "/api/v1/rental-requests";

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private RentalRegistrationService registration;
    @MockitoBean
    private RentalQueryService query;

    private static String json(String key, String title) {
        return "{\"businessKey\":\"%s\",\"title\":\"%s\"}".formatted(key, title);
    }

    @Test
    void validBodyIs201WithLocation() throws Exception {
        MvcResult result = mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON)
                        .content(json("RR-101", "Toyota Camry, 3 days")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.businessKey").value("RR-101"))
                .andExpect(jsonPath("$.status").value("REQUESTED"))
                .andExpect(jsonPath("$.title").value("Toyota Camry, 3 days"))
                .andReturn();
        String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        assertTrue(result.getResponse().getHeader("Location").endsWith(BASE + "/" + id));
        verify(registration).register(any(RentalId.class), eq("RR-101"), eq("Toyota Camry, 3 days"));
    }

    @Test
    void title120CharsIs201() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON)
                        .content(json("RR-102", "x".repeat(120))))
                .andExpect(status().isCreated());
    }

    @Test
    void title121CharsIs400() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON)
                        .content(json("RR-102", "x".repeat(121))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("title size must be between 0 and 120"));
        verifyNoInteractions(registration);
    }

    @Test
    void blankTitleIs400AndServiceIsNotCalled() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-101", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("title must not be blank"));
        verifyNoInteractions(registration);
    }

    @Test
    void spacesOnlyTitleIs400() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-101", "   ")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("title must not be blank"));
        verifyNoInteractions(registration);
    }

    @Test
    void emptyBusinessKeyIs400() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("", "Kia Rio")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("business key must not be blank"));
        verifyNoInteractions(registration);
    }

    @Test
    void bodyThatIsNotJsonIs400() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content("not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("body must be JSON"));
    }

    @Test
    void wrongContentTypeIs415() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.TEXT_PLAIN).content("hello"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value("UNSUPPORTED"));
    }

    @Test
    void getReturns200() throws Exception {
        UUID id = UUID.randomUUID();
        when(query.get(id)).thenReturn(new Rental(id, "RR-101", RentalStatus.APPROVED, "Toyota Camry"));
        mvc.perform(get(BASE + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.businessKey").value("RR-101"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void missingItemIs404() throws Exception {
        UUID id = UUID.randomUUID();
        when(query.get(id)).thenThrow(new RentalNotFound(id.toString()));
        mvc.perform(get(BASE + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void duplicateIs409() throws Exception {
        doThrow(new DuplicateRental("RR-101")).when(registration)
                .register(any(RentalId.class), eq("RR-101"), any());
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json("RR-101", "Again")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE"));
    }

    @Test
    void businessKeyInPathIs400NotUuid() throws Exception {
        mvc.perform(get(BASE + "/RR-101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("id must be a UUID"));
    }

    @Test
    void correlationIdIsEchoedOn400() throws Exception {
        mvc.perform(get(BASE + "/RR-101").header("X-Correlation-Id", "abc-123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.correlationId").value("abc-123"));
    }

    @Test
    void missingCorrelationIdIsMinted() throws Exception {
        mvc.perform(get(BASE + "/RR-101"))
                .andExpect(jsonPath("$.correlationId").isNotEmpty());
    }

    @Test
    void unmappedBugIs500WithoutDetails() throws Exception {
        UUID id = UUID.randomUUID();
        when(query.get(id)).thenThrow(new IllegalStateException("secret internals"));
        mvc.perform(get(BASE + "/" + id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL"))
                .andExpect(jsonPath("$.message").value("unexpected error"));
    }
}
