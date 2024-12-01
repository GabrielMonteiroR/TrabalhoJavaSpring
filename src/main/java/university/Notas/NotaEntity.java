package university.Notas;

import jakarta.persistence.*;
import university.Disciplinas.DisciplinaEntity;
import university.Matricula.MatriculaEntity;

import java.time.LocalDate;

@Entity
@Table(name = "notas")
public class NotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "matricula_id", nullable = false)
    private MatriculaEntity matricula;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private DisciplinaEntity disciplina;

    @Column(nullable = false)
    private Double nota;

    @Column(nullable = false)
    private LocalDate dataLancamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MatriculaEntity getMatricula() {
        return matricula;
    }

    public void setMatricula(MatriculaEntity matricula) {
        this.matricula = matricula;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public DisciplinaEntity getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaEntity disciplina) {
        this.disciplina = disciplina;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
