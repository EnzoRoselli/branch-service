package tesis.company.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tesis.company.exceptions.BranchNotFoundException;
import tesis.company.models.Branch;
import tesis.company.services.BranchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class BranchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    private Branch branch1, branch2;
    private final List<Branch> branches = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(branchController)
                .setControllerAdvice(new ExceptionController())
                .build();

        branch1 = Branch.builder().name("Olavarria 560").userId(1L)
                .address("3460, Avenida Vertiz").city("Mar del Plata").build();
        branch2 = Branch.builder().name("Corrientes 1500")
                .userId(1L).address("4776, Lavalle").city("Mar del Plata").build();
        branches.addAll(Arrays.asList(branch1, branch2));
    }

    @Test
    public void save_ExpectedValues_Ok() throws Exception {
        //given
        given(branchService.save(anyList())).willReturn(branches);

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/branches/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(branches))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().save(branches);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(branches));
    }

    @Test
    public void save_MissingValues_DataIntegrityViolationException() throws Exception {
        //given
        given(branchController.save(anyList())).willThrow(new DataIntegrityViolationException(""));

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/branches/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(branches))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().save(branches);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteById_ExpectedValues_Ok() throws Exception {
        //given
        willDoNothing().given(branchService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/branches/4")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().deleteById(4L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteById_NonexistentId_EmptyResultDataAccessException() throws Exception {
        //given
        willThrow(new EmptyResultDataAccessException(0)).given(branchService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/branches/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().deleteById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getById_ExpectedValues_Ok() throws Exception {
        //given
        given(branchService.getById(anyLong())).willReturn(branch1);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches/4")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getById(4L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(branch1));
    }

    @Test
    public void getById_NonexistentId_BranchNotFoundException() throws Exception {
        //given
        willThrow(new BranchNotFoundException("")).given(branchService).getById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getByUserId_ExpectedValues_Ok() throws Exception {
        //given
        given(branchService.getByUserId(anyLong())).willReturn(branches);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches?userId=40")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getByUserId(40L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(branches));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
