package br.com.escola.mvc.controller;

import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", service.listarTodos());
        return "cursos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", new Curso());
        }
        return "cursos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", service.buscarPorId(id));
        }
        return "cursos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(Curso curso, RedirectAttributes redirectAttributes) {
        try {
            service.salvar(curso);
            redirectAttributes.addFlashAttribute("sucesso", "Curso salvo com sucesso.");
            return "redirect:/cursos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            redirectAttributes.addFlashAttribute("curso", curso);
            String destino = curso.getId() == null
                    ? "/cursos/novo"
                    : "/cursos/editar/" + curso.getId();
            return "redirect:" + destino;
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Curso excluído com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cursos";
    }
}
