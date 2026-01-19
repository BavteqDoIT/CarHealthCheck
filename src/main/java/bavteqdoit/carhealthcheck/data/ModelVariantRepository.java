package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.ModelVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModelVariantRepository extends JpaRepository<ModelVariant, Long> {

    @Query("""
        select mv
        from ModelVariant mv
        where mv.modelType.id = :modelTypeId
          and mv.engineType.id = :engineTypeId
          and mv.bodyType.id = :bodyTypeId
          and (:year is null
               or ((mv.yearFrom is null or mv.yearFrom <= :year)
                   and (mv.yearTo is null or mv.yearTo >= :year)))
        order by
          case when mv.yearFrom is not null or mv.yearTo is not null then 0 else 1 end,
          coalesce(mv.yearFrom, 0) desc,
          coalesce(mv.yearTo, 9999) asc
    """)
    List<ModelVariant> findBestCandidates(
            @Param("modelTypeId") Long modelTypeId,
            @Param("engineTypeId") Long engineTypeId,
            @Param("bodyTypeId") Long bodyTypeId,
            @Param("year") Integer year
    );
}
