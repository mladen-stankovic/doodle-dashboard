package com.doodle.doodledashboard.integration;

import com.doodle.doodledashboard.common.DataConstants;
import com.doodle.doodledashboard.common.UriMappingConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mladen.stankovic on 2020-09-24.
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ActiveProfiles("test")
@SpringBootTest
public class PollControllerIntegrationTests {
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void findByInitiatorEmailSuccessAndDocumentApiTest() throws Exception {
        mockMvc.perform(get(UriMappingConstants.POLLS + UriMappingConstants.INITIATOR + "/" + DataConstants.TEST_INITIATOR_2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.[*].initiator", Matchers.everyItem(is(not(empty())))))
            .andExpect(jsonPath("$.[*].initiator.email", Matchers.everyItem(is(DataConstants.TEST_INITIATOR_2))))
            .andDo(document("findByInitiatorEmailSuccess", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .andReturn();
    }

    @Test
    public void findByInitiatorEmailErrorAndDocumentApiTest() throws Exception {
        mockMvc.perform(get(UriMappingConstants.POLLS + UriMappingConstants.INITIATOR + "/badEmailAddress"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", is("Constraint violation error")))
                .andExpect(jsonPath("$.message", is("findByInitiatorEmail.initiatorEmail: must be a well-formed email address")))
                .andDo(document("findByInitiatorEmailError", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();
    }

    @Test
    public void searchByTitleSuccessAndDocumentApiTest() throws Exception {
        mockMvc.perform(get(UriMappingConstants.POLLS + UriMappingConstants.TITLE + "/" + DataConstants.SEARCH_TERM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[*].title", Matchers.everyItem(is(not(empty())))))
                .andExpect(jsonPath("$.[*].title", Matchers.everyItem(containsString(DataConstants.SEARCH_TERM))))
                .andDo(document("searchByTitleSuccess", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();
    }

}
