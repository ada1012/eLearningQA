package es.ubu.lsi;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.InputStream;


@Controller
public class SpringController{
    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/list")
    public String list() {
        return "list";
    }

    //@ModelAttribute String username
    @GetMapping("/informe")
    public String informe() {
        return "informe";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/manual")
    public String manual() {
        return "manual";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/loading")
    public String loading() {
        return "loading";
    }

    @GetMapping(
            value = "/cabecera.jpg",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] cabecera() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/cabecera.jpg");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/atardecer.jpg",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] atardecer() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/atardecer.jpg");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Logo.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] logo() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Logo.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/FullLogo.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] fullLogo() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/FullLogo.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Cross.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] cross() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Cross.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Check.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] check() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Check.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/eye-slash.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] eyeslash() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/eye-slash.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/InformeFases.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] informeFases() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/InformeFases.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Evolucion.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] evolucion() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Evolucion.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/ListaCursos.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] listaCursos() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/ListaCursos.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Login.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] loginpng() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Login.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/escudoInfor.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] escudoInfor() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/escudoInfor.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Cuestionarios.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] cuestionariospng() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Cuestionarios.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/Foros.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] forospng() throws IOException {
        InputStream in = getClass().getClassLoader()
                .getResourceAsStream("images/Foros.png");
        return IOUtils.toByteArray(in);
    }
}
