package tesis.company.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.company.models.Branch;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    List<Branch> getByUserId(Long userID);
}
