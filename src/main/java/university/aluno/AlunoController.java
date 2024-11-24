package university.aluno;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlunoController {

    private AlunoRepository repository;

    @GetMapping("/alunos")
    public List<AlunoEntity> getAlunos() {
        try {
            var students = repository.findAll();
            if (students.isEmpty()) {
                throw new RuntimeException("No students found.");
            }

            return students;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
