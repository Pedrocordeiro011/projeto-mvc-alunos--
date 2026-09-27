package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public MatriculaService(MatriculaRepository repository, AlunoRepository alunoRepository,
                             CursoRepository cursoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Matricula> listarTodas() {
        return repository.findAllByOrderByIdDesc();
    }

    public Matricula buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada: " + id));
    }

    public Matricula salvar(Matricula matricula) {
        Aluno aluno = buscarAluno(matricula);
        Curso curso = buscarCurso(matricula);

        matricula.setAluno(aluno);
        matricula.setCurso(curso);

        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }

        if (matricula.getId() == null || matricula.getStatus() == null) {
            matricula.setStatus(StatusMatricula.ATIVA);
        }

        validarMatriculaAtivaDuplicada(matricula);

        return repository.save(matricula);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Matrícula não encontrada: " + id);
        }
        repository.deleteById(id);
    }

    private Aluno buscarAluno(Matricula matricula) {
        if (matricula.getAluno() == null || matricula.getAluno().getId() == null) {
            throw new IllegalArgumentException("Selecione um aluno.");
        }
        return alunoRepository.findById(matricula.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
    }

    private Curso buscarCurso(Matricula matricula) {
        if (matricula.getCurso() == null || matricula.getCurso().getId() == null) {
            throw new IllegalArgumentException("Selecione um curso.");
        }
        return cursoRepository.findById(matricula.getCurso().getId())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));
    }

    private void validarMatriculaAtivaDuplicada(Matricula matricula) {
        if (matricula.getStatus() != StatusMatricula.ATIVA) {
            return;
        }

        Long idAtual = matricula.getId() == null ? -1L : matricula.getId();

        boolean jaExiste = repository.existsByAlunoIdAndCursoIdAndStatusAndIdNot(
                matricula.getAluno().getId(),
                matricula.getCurso().getId(),
                StatusMatricula.ATIVA,
                idAtual
        );

        if (jaExiste) {
            throw new IllegalArgumentException("Este aluno já possui uma matrícula ativa neste curso.");
        }
    }
}
