package university.Matricula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Matricula.dto.MatriculaCreateDTO;
import university.Matricula.dto.MatriculaResponseDTO;
import university.Turmas.TurmaEntity;
import university.aluno.AlunoEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepository repository;

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> findAll() {
        var matriculas = repository.findAll();
        if (matriculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<MatriculaResponseDTO> matriculaDTOs = matriculas.stream()
                .map(matricula -> new MatriculaResponseDTO(
                        matricula.getId(),
                        matricula.getAluno().getId(),
                        matricula.getTurma().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(matriculaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(matricula -> new MatriculaResponseDTO(
                        matricula.getId(),
                        matricula.getAluno().getId(),
                        matricula.getTurma().getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> create(@RequestBody MatriculaCreateDTO matriculaDTO) {
        MatriculaEntity matricula = new MatriculaEntity();
        matricula.setAluno(new AlunoEntity(matriculaDTO.alunoId()));
        matricula.setTurma(new TurmaEntity(matriculaDTO.turmaId()));

        MatriculaEntity saved = repository.save(matricula);

        MatriculaResponseDTO responseDTO = new MatriculaResponseDTO(
                saved.getId(),
                saved.getAluno().getId(),
                saved.getTurma().getId()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> update(@PathVariable Integer id, @RequestBody MatriculaCreateDTO matriculaDTO) {
        return repository.findById(id).map(existing -> {
            existing.setAluno(new AlunoEntity(matriculaDTO.alunoId()));
            existing.setTurma(new TurmaEntity(matriculaDTO.turmaId()));
            MatriculaEntity updated = repository.save(existing);

            MatriculaResponseDTO responseDTO = new MatriculaResponseDTO(
                    updated.getId(),
                    updated.getAluno().getId(),
                    updated.getTurma().getId()
            );

            return ResponseEntity.ok(responseDTO);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
