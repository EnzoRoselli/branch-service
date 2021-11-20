package mymarket.branch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mymarket.branch.model.Branch;
import mymarket.branch.service.BranchService;
import mymarket.exception.commons.exception.NotFoundException;
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
        given(branchService.save(branches)).willReturn(branches);

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
        branches.get(0).setName(null);
        given(branchController.save(branches)).willThrow(new DataIntegrityViolationException(""));

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
        willDoNothing().given(branchService).deleteById(branch1.getId());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/branches/" + branch1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().deleteById(branch1.getId());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteById_NonexistentId_EmptyResultDataAccessException() throws Exception {
        Long errorIdNumber = 150L;

        //given
        willThrow(new EmptyResultDataAccessException(0)).given(branchService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/branches/" + errorIdNumber)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().deleteById(errorIdNumber);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getById_ExpectedValues_Ok() throws Exception {
        //given
        given(branchService.getById(branch1.getId())).willReturn(branch1);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches/" + branch1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getById(branch1.getId());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(branch1));
    }

    @Test
    public void getById_NonexistentId_BranchNotFoundException() throws Exception {
        Long errorIdNumber = 150L;

        //given
        willThrow(new NotFoundException("")).given(branchService).getById(errorIdNumber);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches/" + errorIdNumber)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getById(errorIdNumber);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getByUserId_ExpectedValues_Ok() throws Exception {
        Long userId = branches.get(0).getUserId();

        //given
        given(branchService.getByUserId(userId)).willReturn(branches);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/branches?userId=" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(branchService).should().getByUserId(userId);
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
