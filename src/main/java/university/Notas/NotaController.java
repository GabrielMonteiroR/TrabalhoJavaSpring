package university.Notas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Notas.dto.NotaCreateDTO;
import university.Notas.dto.NotaResponseDTO;
import university.Matricula.MatriculaEntity;
import university.Disciplinas.DisciplinaEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaRepository repository;

    @GetMapping
    public ResponseEntity<List<NotaResponseDTO>> findAll() {
        var notas = repository.findAll();
        if (notas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<NotaResponseDTO> notaDTOs = notas.stream()
                .map(nota -> new NotaResponseDTO(
                        nota.getId(),
                        nota.getMatricula().getId(),
                        nota.getDisciplina().getId(),
                        nota.getNota(),
                        nota.getDataLancamento()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(notaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(nota -> new NotaResponseDTO(
                        nota.getId(),
                        nota.getMatricula().getId(),
                        nota.getDisciplina().getId(),
                        nota.getNota(),
                        nota.getDataLancamento()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotaResponseDTO> create(@RequestBody NotaCreateDTO notaDTO) {
        NotaEntity nota = new NotaEntity();
        nota.setMatricula(new MatriculaEntity(notaDTO.matriculaId()));
        nota.setDisciplina(new DisciplinaEntity(notaDTO.disciplinaId()));
        nota.setNota(notaDTO.nota());
        nota.setDataLancamento(notaDTO.dataLancamento());

        NotaEntity saved = repository.save(nota);

        NotaResponseDTO responseDTO = new NotaResponseDTO(
                saved.getId(),
                saved.getMatricula().getId(),
                saved.getDisciplina().getId(),
                saved.getNota(),
                saved.getDataLancamento()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> update(@PathVariable Integer id, @RequestBody NotaCreateDTO notaDTO) {
        return repository.findById(id).map(existing -> {
            existing.setMatricula(new MatriculaEntity(notaDTO.matriculaId()));
            existing.setDisciplina(new DisciplinaEntity(notaDTO.disciplinaId()));
            existing.setNota(notaDTO.nota());
            existing.setDataLancamento(notaDTO.dataLancamento());
            NotaEntity updated = repository.save(existing);

            NotaResponseDTO responseDTO = new NotaResponseDTO(
                    updated.getId(),
                    updated.getMatricula().getId(),
                    updated.getDisciplina().getId(),
                    updated.getNota(),
                    updated.getDataLancamento()
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
