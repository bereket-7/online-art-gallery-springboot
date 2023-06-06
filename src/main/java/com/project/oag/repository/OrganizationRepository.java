package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
