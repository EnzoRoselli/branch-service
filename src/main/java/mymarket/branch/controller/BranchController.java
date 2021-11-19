package mymarket.branch.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import mymarket.branch.model.Branch;
import mymarket.branch.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/branches")
@RestController
@XRayEnabled
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<List<Branch>> save(@RequestBody List<Branch> branches) {
        List<Branch> branchesObjects =  branchService.save(branches);

        return branchesObjects.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.created(getLocation(branchesObjects.get(0))).build();

    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        branchService.deleteById(id);
    }

    @GetMapping("{id}")
    public Branch getById(@PathVariable("id") Long id) {
        return branchService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<Branch>> getByUserId(@RequestParam("userId") Long userId) {
        List<Branch> branchesObjects =  branchService.getByUserId(userId);

        return branchesObjects.isEmpty()?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(branchesObjects);
    }

    private URI getLocation(Branch branch) {

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(branch.getUserId())
                .toUri();
    }
}
