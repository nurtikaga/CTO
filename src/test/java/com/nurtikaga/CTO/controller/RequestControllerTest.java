package com.nurtikaga.CTO.controller;

import com.nurtikaga.CTO.dto.RequestDTO;
import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }
    @Test
    void createRequest_ShouldReturnCreated() throws Exception {
        RequestDTO dto = new RequestDTO();
        dto.setClientName("Test");
        dto.setClientPhone("+1234567890");
        dto.setProblemDescription("Test problem");

        when(requestService.createRequest(any())).thenReturn(new Request());

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientName\":\"Test\",\"clientPhone\":\"+1234567890\",\"problemDescription\":\"Test problem\"}"))
                .andExpect(status().isOk());
    }
}
