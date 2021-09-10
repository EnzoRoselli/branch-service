package mymarket.branch.controllers;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import mymarket.branch.models.Branch;
import mymarket.branch.services.BranchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/branches")
@RestController
@XRayEnabled
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public List<Branch> save(@RequestBody List<Branch> branches) {
        return branchService.save(branches);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        branchService.deleteById(id);
    }

    @GetMapping("{id}")
    public Branch getById(@PathVariable("id") Long id) { return branchService.getById(id); }

    @GetMapping
    public List<Branch> getByUserId(@RequestParam("userId") Long userId){
        return branchService.getByUserId(userId);
    }
}
