package br.com.escola.mvc;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ProjetoMvcAlunosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoMvcAlunosApplication.class, args);
    }

    @Bean
    CommandLineRunner carregarDadosIniciais(AlunoRepository alunoRepository,
                                             CursoRepository cursoRepository,
                                             MatriculaRepository matriculaRepository) {
        return args -> {
            if (alunoRepository.count() == 0) {
                Aluno ana = alunoRepository.save(new Aluno(null, "Ana Souza", "ana@email.com", "11.111.111-1"));
                Aluno carlos = alunoRepository.save(new Aluno(null, "Carlos Lima", "carlos@email.com", "22.222.222-2"));
                Aluno mariana = alunoRepository.save(new Aluno(null, "Mariana Silva", "mariana@email.com", "33.333.333-3"));

                Curso informatica = cursoRepository.save(new Curso(null, "Informática para Internet",
                        "Curso técnico voltado para desenvolvimento web.", 1200));
                Curso desenvolvimento = cursoRepository.save(new Curso(null, "Desenvolvimento de Sistemas",
                        "Curso técnico voltado para desenvolvimento de software.", 1400));

                matriculaRepository.save(new Matricula(null, ana, informatica, LocalDate.now(), StatusMatricula.ATIVA));
                matriculaRepository.save(new Matricula(null, carlos, informatica, LocalDate.now(), StatusMatricula.ATIVA));
                matriculaRepository.save(new Matricula(null, mariana, desenvolvimento, LocalDate.now(), StatusMatricula.ATIVA));
            }
        };
    }
}
