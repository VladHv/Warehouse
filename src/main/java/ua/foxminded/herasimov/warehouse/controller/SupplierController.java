package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.impl.SupplierServiceImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierServiceImpl service;

    @Autowired
    public SupplierController(SupplierServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> findAllSuppliers() {
        List<Supplier> suppliers = service.findAll();
        if (suppliers != null && !suppliers.isEmpty()) {
            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> createSupplier(@Valid @RequestBody Supplier supplier) {
        service.create(supplier);
        return new ResponseEntity<>("Supplier is valid", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>("Supplier deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> findSupplierById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable("id") Integer id,
                                                   @Valid @ModelAttribute("supplier") Supplier supplier) {
        return new ResponseEntity<>(service.update(supplier, id), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
