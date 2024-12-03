package university.Notas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaRepository extends JpaRepository<NotaEntity, Integer> {
    List<NotaEntity> findByDisciplinaId(Integer disciplinaId);

    @Query("SELECT n FROM NotaEntity n WHERE n.matricula.turma.id = :turmaId")
    List<NotaEntity> findByTurmaId(@Param("turmaId") Integer turmaId);

    @Query("SELECT n FROM NotaEntity n WHERE n.matricula.aluno.id = :alunoId")
    List<NotaEntity> findByAlunoId(@Param("alunoId") Integer alunoId);
}