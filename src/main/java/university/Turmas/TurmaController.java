package university.Turmas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Notas.NotaRepository;
import university.Turmas.dto.TurmaCreateDTO;
import university.Turmas.dto.TurmaResponseDTO;
import university.Cursos.CursoEntity;
import university.aluno.dto.NotaDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository repository;
    @Autowired
    private NotaRepository notaRepository;

    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> findAll() {
        var turmas = repository.findAll();
        if (turmas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TurmaResponseDTO> turmaDTOs = turmas.stream()
                .map(turma -> new TurmaResponseDTO(
                        turma.getId(),
                        turma.getAno(),
                        turma.getSemestre(),
                        turma.getCurso().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(turmaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(turma -> new TurmaResponseDTO(
                        turma.getId(),
                        turma.getAno(),
                        turma.getSemestre(),
                        turma.getCurso().getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TurmaResponseDTO> create(@RequestBody TurmaCreateDTO turmaDTO) {
        TurmaEntity turma = new TurmaEntity();
        turma.setAno(turmaDTO.ano());
        turma.setSemestre(turmaDTO.semestre());
        turma.setCurso(new CursoEntity(turmaDTO.cursoId()));

        TurmaEntity saved = repository.save(turma);

        TurmaResponseDTO responseDTO = new TurmaResponseDTO(
                saved.getId(),
                saved.getAno(),
                saved.getSemestre(),
                saved.getCurso().getId()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> update(@PathVariable Integer id, @RequestBody TurmaCreateDTO turmaDTO) {
        return repository.findById(id).map(existing -> {
            existing.setAno(turmaDTO.ano());
            existing.setSemestre(turmaDTO.semestre());
            existing.setCurso(new CursoEntity(turmaDTO.cursoId()));
            TurmaEntity updated = repository.save(existing);

            TurmaResponseDTO responseDTO = new TurmaResponseDTO(
                    updated.getId(),
                    updated.getAno(),
                    updated.getSemestre(),
                    updated.getCurso().getId()
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

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<NotaDTO>> getNotasByTurma(@PathVariable Integer id) {
        var notas = notaRepository.findByTurmaId(id).stream()
                .map(nota -> new NotaDTO(
                        nota.getDisciplina().getNome(),
                        nota.getNota(),
                        nota.getDataLancamento()
                ))
                .collect(Collectors.toList());

        return notas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(notas);
    }


}
