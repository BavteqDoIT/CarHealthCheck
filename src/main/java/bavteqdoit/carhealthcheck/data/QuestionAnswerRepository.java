package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer,Long> {
    @Query("""
        select qa from QuestionAnswer qa
        join fetch qa.question q
        left join fetch qa.selectedOption so
        where qa.car.id = :carId
    """)
    List<QuestionAnswer> findAllByCarIdWithDetails(@Param("carId") Long carId);
}
