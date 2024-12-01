package university.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Integer> {
    Optional<AlunoEntity> findByMatricula(String matricula);
}
