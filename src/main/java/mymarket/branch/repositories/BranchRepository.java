package mymarket.branch.repositories;

import mymarket.branch.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    List<Branch> getByUserId(Long userID);
}
