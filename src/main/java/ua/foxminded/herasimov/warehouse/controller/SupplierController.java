package ua.foxminded.herasimov.warehouse.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
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

    @ApiOperation(value = "Find all suppliers",
                  notes = "This method returns all suppliers of warehouse")
    @GetMapping
    public ResponseEntity<List<Supplier>> findAllSuppliers() {
        List<Supplier> suppliers = service.findAll();
        if (suppliers != null && !suppliers.isEmpty()) {
            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Create new supplier",
                  notes = "This method requires valid supplier")
    @PostMapping
    public ResponseEntity<String> createSupplier(@ApiParam("Supplier to add. Cannot be null or empty")
                                                 @Valid @RequestBody Supplier supplier) {
        service.create(supplier);
        return new ResponseEntity<>("Supplier is valid", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete supplier by id",
                  notes = "Remove supplier from warehouse")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@ApiParam(name = "id",
                                                           type = "Integer",
                                                           value = "ID value for the supplier",
                                                           example = "1",
                                                           required = true)
                                                 @PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>("Supplier deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Find supplier by id",
                  notes = "Returns a single supplier. Provide an id to look up specific supplier")
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> findSupplierById(@ApiParam(name = "id",
                                                               type = "Integer",
                                                               value = "ID value for the supplier",
                                                               example = "1",
                                                               required = true)
                                                     @PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Update existed supplier",
                  notes = "This method updates valid supplier per id")
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@ApiParam(name = "id",
                                                             type = "Integer",
                                                             value = "ID value for the supplier",
                                                             example = "1",
                                                             required = true)
                                                   @PathVariable("id") Integer id,
                                                   @ApiParam("Supplier to update. Cannot be null or empty")
                                                   @Valid @RequestBody Supplier supplier) {
        return new ResponseEntity<>(service.update(supplier, id), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
