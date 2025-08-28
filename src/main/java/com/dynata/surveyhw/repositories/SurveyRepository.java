package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Survey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long>, SurveyCustomRepository {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO survey (survey_id, name, expected_completes, completion_points, filtered_points)
            VALUES (:#{#s.surveyId}, :#{#s.name}, :#{#s.expectedCompletes}, :#{#s.completionPoints}, :#{#s.filteredPoints})
            ON CONFLICT (survey_id)
            DO UPDATE SET name = EXCLUDED.name,
                        expected_completes = EXCLUDED.expected_completes,
                        completion_points = EXCLUDED.completion_points,
                        filtered_points = EXCLUDED.filtered_points
            """, nativeQuery = true)
    void upsertSurvey(Survey s);

    @Query("SELECT s FROM Survey s JOIN Participation p ON s.surveyId = p.surveyId "
            + "WHERE p.memberId = :memberId")
    List<Survey> findCompletionPointsByMemberId(@Param("memberId") Long memberId);
}
