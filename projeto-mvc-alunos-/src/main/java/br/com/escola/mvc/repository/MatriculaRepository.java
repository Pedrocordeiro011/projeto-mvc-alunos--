package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findAllByOrderByIdDesc();

    boolean existsByAlunoIdAndCursoIdAndStatusAndIdNot(Long alunoId, Long cursoId, StatusMatricula status, Long id);

    boolean existsByAlunoIdAndCursoIdAndStatus(Long alunoId, Long cursoId, StatusMatricula status);

    boolean existsByAlunoId(Long alunoId);

    boolean existsByCursoId(Long cursoId);
}
