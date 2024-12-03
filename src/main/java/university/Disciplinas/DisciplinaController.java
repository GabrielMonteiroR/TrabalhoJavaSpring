package university.Disciplinas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.Cursos.CursoEntity;
import university.Disciplinas.dto.DisciplinaCreateDTO;
import university.Disciplinas.dto.DisciplinaResponseDTO;
import university.Notas.NotaRepository;
import university.Professor.ProfessorEntity;
import university.aluno.dto.NotaDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repository;
    @Autowired
    private NotaRepository notaRepository;

    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> findAll() {
        var disciplinas = repository.findAll();
        if (disciplinas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<DisciplinaResponseDTO> disciplinaDTOs = disciplinas.stream()
                .map(disciplina -> new DisciplinaResponseDTO(
                        disciplina.getId(),
                        disciplina.getNome(),
                        disciplina.getCodigo(),
                        disciplina.getCurso().getId(),
                        disciplina.getProfessor().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(disciplinaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(disciplina -> new DisciplinaResponseDTO(
                        disciplina.getId(),
                        disciplina.getNome(),
                        disciplina.getCodigo(),
                        disciplina.getCurso().getId(),
                        disciplina.getProfessor().getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> create(@RequestBody DisciplinaCreateDTO disciplinaDTO) {
        DisciplinaEntity disciplina = new DisciplinaEntity();
        disciplina.setNome(disciplinaDTO.nome());
        disciplina.setCodigo(disciplinaDTO.codigo());
        disciplina.setCurso(new CursoEntity(disciplinaDTO.cursoId()));
        disciplina.setProfessor(new ProfessorEntity(disciplinaDTO.professorId()));

        DisciplinaEntity saved = repository.save(disciplina);

        DisciplinaResponseDTO responseDTO = new DisciplinaResponseDTO(
                saved.getId(),
                saved.getNome(),
                saved.getCodigo(),
                saved.getCurso().getId(),
                saved.getProfessor().getId()
        );

        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> update(@PathVariable Integer id, @RequestBody DisciplinaCreateDTO disciplinaDTO) {
        return repository.findById(id).map(existing -> {
            existing.setNome(disciplinaDTO.nome());
            existing.setCodigo(disciplinaDTO.codigo());
            existing.setCurso(new CursoEntity(disciplinaDTO.cursoId()));
            existing.setProfessor(new ProfessorEntity(disciplinaDTO.professorId()));
            DisciplinaEntity updated = repository.save(existing);

            DisciplinaResponseDTO responseDTO = new DisciplinaResponseDTO(
                    updated.getId(),
                    updated.getNome(),
                    updated.getCodigo(),
                    updated.getCurso().getId(),
                    updated.getProfessor().getId()
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
    public ResponseEntity<List<NotaDTO>> getNotasByDisciplina(@PathVariable Integer id) {
        var notas = notaRepository.findByDisciplinaId(id).stream()
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
