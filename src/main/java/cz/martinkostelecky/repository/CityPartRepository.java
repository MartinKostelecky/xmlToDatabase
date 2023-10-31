package cz.martinkostelecky.repository;

import cz.martinkostelecky.model.CityPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityPartRepository extends JpaRepository<CityPartEntity, Long> {
}
