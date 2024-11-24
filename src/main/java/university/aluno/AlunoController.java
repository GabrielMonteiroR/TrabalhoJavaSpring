package university.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoEntity>> getAlunos() {
        try {
            var students = repository.findAll();

            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(students);
        } catch (Exception e) {
            System.err.println("Erro ao buscar alunos: " + e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}
