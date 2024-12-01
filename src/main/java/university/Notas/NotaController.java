package university.Notas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaRepository repository;

    @GetMapping
    public ResponseEntity<List<NotaEntity>> findAll() {
        var notas = repository.findAll();
        return notas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaEntity> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotaEntity> create(@RequestBody NotaEntity nota) {
        NotaEntity saved = repository.save(nota);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaEntity> update(@PathVariable Integer id, @RequestBody NotaEntity nota) {
        return repository.findById(id).map(existing -> {
            existing.setMatricula(nota.getMatricula());
            existing.setDisciplina(nota.getDisciplina());
            existing.setNota(nota.getNota());
            existing.setDataLancamento(nota.getDataLancamento());
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
