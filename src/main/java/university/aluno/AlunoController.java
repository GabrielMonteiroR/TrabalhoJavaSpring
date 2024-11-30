package university.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoDTO>> getAlunos() {
        try {
            var students = repository.findAll();

            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<AlunoDTO> studentDTOs = students.stream()
                    .map(student -> new AlunoDTO(
                            student.getId(),
                            student.getNome(),
                            student.getEmail()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(studentDTOs);
        } catch (Exception e) {
            System.err.println("Erro ao buscar alunos: " + e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}
