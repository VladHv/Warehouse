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
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.service.impl.GoodsItemServiceImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goodsItems")
public class GoodsItemController {

    private final GoodsItemServiceImpl service;

    @Autowired
    public GoodsItemController(GoodsItemServiceImpl service) {
        this.service = service;
    }

    @ApiOperation(value = "Find all goods items",
                  notes = "This method returns all goods items in warehouse")
    @GetMapping
    public ResponseEntity<List<GoodsItem>> getAllGoodsItems() {
        List<GoodsItem> goodsItems = service.findAll();
        if (goodsItems != null && !goodsItems.isEmpty()) {
            return new ResponseEntity<>(goodsItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Create new goods item",
                  notes = "This method requires valid goods item")
    @PostMapping
    public ResponseEntity<String> createGoodsItem(@ApiParam("Goods item to add. Cannot be null or empty")
                                                  @Valid @RequestBody GoodsItem goodsItem) {
        service.create(goodsItem);
        return new ResponseEntity<>("GoodsItem is valid", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete goods item by id",
                  notes = "Remove goods item from warehouse")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsItem(@ApiParam(name = "id",
                                                            type = "Integer",
                                                            value = "ID value for the goods item",
                                                            example = "1",
                                                            required = true)
                                                  @PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>("GoodsItem deleted successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Find goods item by id",
                  notes = "Returns a single goods item. Provide an id to look up specific goods item")
    @GetMapping("/{id}")
    public ResponseEntity<GoodsItem> findGoodsItemById(@ApiParam(name = "id",
                                                                 type = "Integer",
                                                                 value = "ID value for the goods",
                                                                 example = "1",
                                                                 required = true)
                                                       @PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
        } catch (ServiceException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Update existed goods item",
                  notes = "This method updates valid goods item per id")
    @PutMapping("{id}")
    public ResponseEntity<GoodsItem> updateGoodsItem(@ApiParam(name = "id",
                                                               type = "Integer",
                                                               value = "ID value for the goods",
                                                               example = "1",
                                                               required = true)
                                                     @PathVariable("id") Integer id,
                                                     @ApiParam("Goods item to update. Cannot be null or empty")
                                                     @Valid @RequestBody GoodsItem goodsItem) {
        return new ResponseEntity<>(service.update(goodsItem, id), HttpStatus.OK);
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
