package university.aluno.dto;

import java.time.LocalDate;

public record NotaDTO(String disciplina, Double nota, LocalDate dataLancamento) {}

