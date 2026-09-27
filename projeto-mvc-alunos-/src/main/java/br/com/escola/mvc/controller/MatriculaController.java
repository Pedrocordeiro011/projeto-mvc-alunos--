package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.service.AlunoService;
import br.com.escola.mvc.service.CursoService;
import br.com.escola.mvc.service.MatriculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService service;
    private final AlunoService alunoService;
    private final CursoService cursoService;

    public MatriculaController(
            MatriculaService service,
            AlunoService alunoService,
            CursoService cursoService) {
        this.service = service;
        this.alunoService = alunoService;
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("matriculas", service.listarTodas());
        return "matriculas/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("matricula")) {
            model.addAttribute("matricula", new Matricula());
        }
        carregarListasDeApoio(model);
        return "matriculas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("matricula")) {
            model.addAttribute("matricula", service.buscarPorId(id));
        }
        carregarListasDeApoio(model);
        return "matriculas/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(Matricula matricula, RedirectAttributes redirectAttributes) {
        try {
            service.salvar(matricula);
            redirectAttributes.addFlashAttribute("sucesso", "Matrícula salva com sucesso.");
            return "redirect:/matriculas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            redirectAttributes.addFlashAttribute("matricula", matricula);
            String destino = matricula.getId() == null
                    ? "/matriculas/novo"
                    : "/matriculas/editar/" + matricula.getId();
            return "redirect:" + destino;
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Matrícula excluída com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/matriculas";
    }

    private void carregarListasDeApoio(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("cursos", cursoService.listarTodos());
        model.addAttribute("statusList", StatusMatricula.values());
    }
}
