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
        try {
            var students = repository.findAll();

            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<AlunoListDTO> studentDTOs = students.stream()
                    .map(student -> new AlunoListDTO(
                            student.getNome(),
                            student.getEmail(),
                            student.getMatricula()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(studentDTOs);
        } catch (Exception e) {
            System.err.println("Erro ao buscar alunos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoListDTO> findStudentById(@PathVariable Integer id) {
        try {
            var student = this.repository.findById(id);

            if (student.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            AlunoEntity aluno = student.get();
            AlunoListDTO alunoDTO = new AlunoListDTO(
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.getMatricula()
            );

            return ResponseEntity.ok(alunoDTO);
        } catch (Exception e) {
            System.err.println("Erro ao buscar aluno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<AlunoListDTO> createAluno(@RequestBody AlunoCreateDTO alunoDTO) {
        try {
            Optional<AlunoEntity> alunoExists = repository.findByMatricula(alunoDTO.matricula());
            if (alunoExists.isPresent()) {
                return ResponseEntity.status(409).body(null);
            }

            AlunoEntity alunoEntity = new AlunoEntity();
            alunoEntity.setNome(alunoDTO.nome());
            alunoEntity.setEmail(alunoDTO.email());
            alunoEntity.setMatricula(alunoDTO.matricula());

            AlunoEntity savedAluno = repository.save(alunoEntity);
            AlunoListDTO responseDTO = new AlunoListDTO(
                    savedAluno.getNome(),
                    savedAluno.getEmail(),
                    savedAluno.getMatricula()
            );

            return ResponseEntity.status(201).body(responseDTO);
        } catch (Exception e) {
            System.err.println("Erro ao criar aluno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoListDTO> updateAluno(@PathVariable Integer id, @RequestBody AlunoCreateDTO alunoDTO) {
        try {
            Optional<AlunoEntity> optionalAluno = repository.findById(id);

            if (optionalAluno.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Optional<AlunoEntity> existingAluno = repository.findByMatricula(alunoDTO.matricula());
            if (existingAluno.isPresent() && !existingAluno.get().getId().equals(id)) {
                return ResponseEntity.status(409).body(null);
            }

            AlunoEntity alunoEntity = optionalAluno.get();
            alunoEntity.setNome(alunoDTO.nome());
            alunoEntity.setEmail(alunoDTO.email());
            alunoEntity.setMatricula(alunoDTO.matricula());

            AlunoEntity updatedAluno = repository.save(alunoEntity);
            AlunoListDTO responseDTO = new AlunoListDTO(
                    updatedAluno.getNome(),
                    updatedAluno.getEmail(),
                    updatedAluno.getMatricula()
            );

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Integer id) {
        try {
            Optional<AlunoEntity> optionalAluno = repository.findById(id);

            if (optionalAluno.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Erro ao excluir aluno: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
