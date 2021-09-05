package tesis.company.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tesis.company.models.Branch;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    @Query(value = "Select * from branches where user_id=?1",nativeQuery = true)
    List<Branch> getByUserID(Long userID);
}
