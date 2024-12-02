package university.aluno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.aluno.dto.AlunoCreateDTO;
import university.aluno.dto.AlunoListDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @GetMapping
    public ResponseEntity<List<AlunoListDTO>> findAlunos() {
        List<AlunoListDTO> students = repository.findAll()
                .stream()
                .map(student -> new AlunoListDTO(
                        student.getNome(),
                        student.getEmail(),
                        student.getMatricula()
                ))
                .collect(Collectors.toList());

        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoListDTO> findStudentById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(aluno -> ResponseEntity.ok(new AlunoListDTO(
                        aluno.getNome(),
                        aluno.getEmail(),
                        aluno.getMatricula()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoListDTO> createAluno(@RequestBody AlunoCreateDTO alunoDTO) {
        if (repository.existsByMatricula(alunoDTO.matricula())) {
            return ResponseEntity.status(409).body(null);
        }

        AlunoEntity alunoEntity = new AlunoEntity();
        alunoEntity.setNome(alunoDTO.nome());
        alunoEntity.setEmail(alunoDTO.email());
        alunoEntity.setMatricula(alunoDTO.matricula());

        AlunoEntity savedAluno = repository.save(alunoEntity);
        return ResponseEntity.status(201).body(new AlunoListDTO(
                savedAluno.getNome(),
                savedAluno.getEmail(),
                savedAluno.getMatricula()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoListDTO> updateAluno(@PathVariable Integer id, @RequestBody AlunoCreateDTO alunoDTO) {
        Optional<AlunoEntity> alunoOpt = repository.findById(id);

        if (alunoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (repository.existsByMatriculaAndIdNot(alunoDTO.matricula(), id)) {
            return ResponseEntity.status(409).body(null);
        }

        AlunoEntity alunoEntity = alunoOpt.get();
        alunoEntity.setNome(alunoDTO.nome());
        alunoEntity.setEmail(alunoDTO.email());
        alunoEntity.setMatricula(alunoDTO.matricula());

        AlunoEntity updatedAluno = repository.save(alunoEntity);
        return ResponseEntity.ok(new AlunoListDTO(
                updatedAluno.getNome(),
                updatedAluno.getEmail(),
                updatedAluno.getMatricula()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
