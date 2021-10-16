package mymarket.branch.service;

import lombok.RequiredArgsConstructor;
import mymarket.branch.exception.BranchNotFoundException;
import mymarket.branch.model.Branch;
import mymarket.branch.repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public List<Branch> save(List<Branch> branches){ return branchRepository.saveAll(branches); }

    public void deleteById(Long id){ branchRepository.deleteById(id); }

    public Branch getById(Long id){
        return branchRepository.findById(id).orElseThrow(() ->
                new BranchNotFoundException("Branch with id " + id + " not found."));
    }

    public List<Branch> getByUserId(Long userId){ return branchRepository.getByUserId(userId); }
}
