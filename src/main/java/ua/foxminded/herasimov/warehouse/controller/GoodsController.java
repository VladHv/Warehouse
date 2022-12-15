package ua.foxminded.herasimov.warehouse.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
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

    @ApiOperation(value = "Find all goods",
                  notes = "This method returns all goods positions")
    @GetMapping
    public ResponseEntity<List<Goods>> getAllGoods() {
        List<Goods> goods = service.findAll();
        if (goods != null && !goods.isEmpty()) {
            return new ResponseEntity<>(goods, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Create new goods",
                  notes = "This method requires valid goods")
    @PostMapping
    public ResponseEntity<String> createGoods(@ApiParam("Goods to add. Cannot be null or empty")
                                              @Valid @RequestBody Goods goods) {
        service.create(goods);
        return new ResponseEntity<>("Goods is valid", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete goods by id",
                  notes = "Remove goods position from warehouse")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoods(@ApiParam(name = "id",
                                                        type = "Integer",
                                                        value = "ID value for the goods",
                                                        example = "1",
                                                        required = true)
                                              @PathVariable("id") Integer id,
                                              @ApiParam("Any message you can add to the end of response message")
                                              @Size(min = 2, max = 5, message = "Length should be from 2 to 5")
                                              @RequestParam(value = "message", required = false) String message) {
        service.delete(id);
        if (message == null) message = "";
        return new ResponseEntity<>("Goods deleted successfully" + message, HttpStatus.OK);
    }

    @ApiOperation(value = "Find goods by id",
                  notes = "Returns a single goods. Provide an id to look up specific goods")
    @GetMapping("/{id}")
    public ResponseEntity<Goods> findGoodsById(@ApiParam(name = "id",
                                                         type = "Integer",
                                                         value = "ID value for the goods",
                                                         example = "1",
                                                         required = true) @PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Update existed goods",
                  notes = "This method updates valid goods per id")
    @PutMapping("/{id}")
    public ResponseEntity<Goods> updateGoods(@ApiParam(name = "id",
                                                       type = "Integer",
                                                       value = "ID value for the goods",
                                                       example = "1",
                                                       required = true)
                                             @PathVariable("id") Integer id,
                                             @ApiParam("Goods to update. Cannot be null or empty")
                                             @Valid @RequestBody Goods goods) {
        return new ResponseEntity<>(service.update(goods, id), HttpStatus.OK);
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
