package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsServiceImpl;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
@Validated
public class GoodsController {

    private final GoodsServiceImpl service;

    @Autowired
    public GoodsController(GoodsServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Goods>> getAllGoods() {
        List<Goods> goods = service.findAll();
        if (goods != null && !goods.isEmpty()) {
            return new ResponseEntity<>(goods, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> createGoods(@Valid @RequestBody Goods goods) {
        service.create(goods);
        return new ResponseEntity<>("Goods is valid", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoods(@PathVariable("id") Integer id,
                                              @Size(min = 2, max = 5, message = "Length should be from 2 to 5")
                                              @RequestParam(value = "message", required = false) String message) {
        service.delete(id);
        return new ResponseEntity<>("Goods deleted successfully" + message, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goods> findGoodsById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goods> updateGoods(@PathVariable("id") Integer id, @Valid @RequestBody Goods goods) {
        return new ResponseEntity<>(service.update(goods, id), HttpStatus.OK);
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
