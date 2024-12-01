package university.Disciplinas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository repository;

    @GetMapping
    public ResponseEntity<List<DisciplinaEntity>> findAll() {
        var disciplinas = repository.findAll();
        return disciplinas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaEntity> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DisciplinaEntity> create(@RequestBody DisciplinaEntity disciplina) {
        DisciplinaEntity saved = repository.save(disciplina);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaEntity> update(@PathVariable Integer id, @RequestBody DisciplinaEntity disciplina) {
        return repository.findById(id).map(existing -> {
            existing.setNome(disciplina.getNome());
            existing.setCodigo(disciplina.getCodigo());
            existing.setCurso(disciplina.getCurso());
            existing.setProfessor(disciplina.getProfessor());
            repository.save(existing);
            return ResponseEntity.ok(existing);
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
