package tesis.company.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tesis.company.exceptions.BranchNotFoundException;
import tesis.company.models.Branch;
import tesis.company.repositories.BranchRepository;

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
