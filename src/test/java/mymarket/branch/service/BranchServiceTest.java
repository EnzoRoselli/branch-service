package mymarket.branch.service;

import mymarket.branch.model.Branch;
import mymarket.branch.repository.BranchRepository;
import mymarket.exception.commons.exception.NotFoundException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private Branch branch1, branch2;
    private final List<Branch> branches = new ArrayList<>();

    @BeforeEach
    void setUp() {
        branch1 = Branch.builder().name("Olavarria 560").userId(1L)
                .address("3460, Avenida Vertiz").city("Mar del Plata").build();
        branch2 = Branch.builder().name("Corrientes 1500")
                .userId(1L).address("4776, Lavalle").city("Mar del Plata").build();
        branches.addAll(Arrays.asList(branch1, branch2));
    }

    @Test
    public void save_ExpectedValues_Ok() {
        //given
        given(branchRepository.saveAll(anyList())).willReturn(branches);

        //when
        List<Branch> branchList = branchService.save(branches);

        //then
        then(branchRepository).should().saveAll(branches);
        assertThat(branchList).isNotNull();
        assertThat(branchList).hasSize(2);
        assertThat(branchList).isEqualTo(branches);
    }

    @Test
    public void deleteById_ExpectedValues_Ok() {
        //given
        willDoNothing().given(branchRepository).deleteById(anyLong());

        //when
        branchService.deleteById(1L);
        branchService.deleteById(2L);
        branchService.deleteById(3L);

        //then
        then(branchRepository).should(times(3)).deleteById(anyLong());
    }

    @Test
    public void getById_ExpectedValues_Ok() {
        //given
        Optional<Branch> branch = Optional.of(branch1);
        given(branchRepository.findById(anyLong())).willReturn(branch);

        //when
        Branch branchFromRepository = branchService.getById(1L);

        //then
        then(branchRepository).should().findById(1L);
        assertThat(branchFromRepository).isNotNull();
        assertThat(branchFromRepository).isEqualTo(branch.get());
    }

    @Test
    public void getById_NonexistentId_BranchNotFoundException() {
        //given
        given(branchRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        when(() -> branchService.getById(1L));

        //then
        BDDAssertions.then(caughtException())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Branch with id 1 not found.")
                .hasNoCause();
    }

    @Test
    public void getByUserId_ExpectedValues_Ok() {
        //given
        given(branchRepository.getByUserId(anyLong())).willReturn(branches);

        //when
        List<Branch> branchList = branchService.getByUserId(1L);

        //then
        then(branchRepository).should().getByUserId(1L);
        assertThat(branchList).isNotNull();
        assertThat(branchList).hasSize(2);
        assertThat(branchList).isEqualTo(branches);
    }
}
