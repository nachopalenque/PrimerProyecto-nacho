package com.iesjuanbosco.ejemploweb.controller;

import com.iesjuanbosco.ejemploweb.entity.Producto;
import com.iesjuanbosco.ejemploweb.repository.ProductoRepository;
import jakarta.validation.Valid;
import org.hibernate.query.NativeQuery;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Para que Spring sepa que esta clase es un controlador tenemos que añadir la anotación @Controller antes de la clase
@Controller
public class ProductoController {

    //Para acceder al repositorio creamos una propiedad y la asignamos en el constructor
    private ProductoRepository productoRepository;

    public ProductoController(ProductoRepository repository){
        this.productoRepository = repository;
    }

    /* Con la anotación GetMapping le indicamos a Spring que el siguiente método
       se va a ejecutar cuando el usuario acceda a la URL http://localhost/productos */
    @GetMapping("/productos")
    public String findAll(Model model){
        List<Producto> productos = this.productoRepository.findAll();

        //Pasamos los datos a la vista
        model.addAttribute("productos",productos);

        return "producto-list";
    }

    //borra un producto a partir de la ruta
    @GetMapping("/productos/del/{id}")
    public String Delete(@PathVariable Long id){

        //Borrar el producto usando el repositorio
        productoRepository.deleteById(id);
        //redirigir al listado de productos
        return "redirect: /productos";
    }

    //---------------------------------------------
    //por get muestra la vista
    @GetMapping("/productos/new")
    public String newProducto(){

        //redirigir al listado de productos
        return "producto-new";
    }
    //por post para mandar los datos
    @PostMapping("/productos/new")
    public String newProductoInsert(@Valid Producto producto, BindingResult bindingResult, Model model)
    {
        //Si hay errores de validacion volvemos a mostrar el formulario
        if (bindingResult.hasErrors()) {

            return "producto-new";
        }

        productoRepository.save(producto);
        //redirigir al listado de productos
        return "redirect:/productos";
    }



    //----------------------------------------------

    //por get muestra la vista
    @GetMapping("/productos/edit/{id}")
    public String editProducto(@PathVariable Long id, Model model){
        //optional para contemplar la posibilidad de que no exista producto con ese id
        // además el optional contiene metodos como el isPresent
        Optional<Producto> producto = productoRepository.findById(id);
        if(producto.isPresent()){

            //para pasarle a la vista el objeto
            model.addAttribute("producto",producto.get());
            return "producto-edit";
        }

        //redirigir al listado de productos
        return "redirect:/productos";
    }

    //por post para mandar los datos
    @PostMapping("/productos/edit/{id}")
    public String editProductoUpdate(@PathVariable Long id ,Producto producto)
    {
        producto.setId(id);
        productoRepository.save(producto);
        //redirigir al listado de productos
        return "redirect:/productos";
    }
    //----------------------------------------------







    @GetMapping("/productos/upd/{id}")
    public String Update(@PathVariable Long id){

        //Borrar el producto usando el repositorio
        productoRepository.deleteById(id);
        //redirigir al listado de productos
        return "redirect: /productos";
    }
    @GetMapping("/productos/sel/{id}")
    public String Select(@PathVariable Long id, Model model){
        List<Producto> productos = new ArrayList<Producto>();
        Producto pid = new Producto();

        //Borrar el producto usando el repositorio
        pid = productoRepository.getReferenceById(id);
        productos.add(pid);
        //redirigir al listado de productos
        //Pasamos los datos a la vista
        model.addAttribute("productos",productos);
        return "redirect: /productos";

    }

    @GetMapping("/productos/add")
    public String add(){
        List<Producto> productos = new ArrayList<Producto>();
        Producto p1 = new Producto(null, "Producto 1", 20, 45.5);
        Producto p2 = new Producto(null, "Producto 2", 50, 5.0);
        Producto p3 = new Producto(null, "Producto 3", 30, 50.5);
        Producto p4 = new Producto(null, "Producto 4", 10, 30.0);
        productos.add(p1);
        productos.add(p2);
        productos.add(p3);
        productos.add(p4);

        //Guardamos todos los productos en la base de datos utilizando el objeto productoRepository
        this.productoRepository.saveAll(productos);

        //Redirige al controlador /productos
        return "redirect:/productos";

    }

    @GetMapping("/") //anotacion que indica la URL localhost:8080/productos2
    @ResponseBody // anotacion que le indica que no pase por el motor de plantillas tinyleaf sin que voy a devolver el html directamente
    public String index(){
        StringBuilder HTML = new StringBuilder("<html><body>");

        List<Producto> productos = this.productoRepository.findAll();

        productos.forEach(producto -> {
            HTML.append("<p>" + producto.getTitulo() + "<p>");

        });
        HTML.append("</body></html>");
        return HTML.toString();
    }


}
