package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDto;
import ua.foxminded.herasimov.warehouse.dto.impl.GoodsItemDtoMapper;
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
    private final GoodsItemDtoMapper dtoMapper;

    @Autowired
    public GoodsItemController(GoodsItemServiceImpl service, GoodsItemDtoMapper dtoMapper) {
        this.service = service;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<GoodsItem>> getAllGoodsItems() {
        List<GoodsItem> goodsItems = service.findAll();
        if (goodsItems != null && !goodsItems.isEmpty()) {
            return new ResponseEntity<>(goodsItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> createGoodsItem(@Valid @RequestBody GoodsItemDto goodsItemDto) {
        service.create(dtoMapper.toEntity(goodsItemDto));
        return new ResponseEntity<>("GoodsItem is valid", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGoodsItem(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>("GoodsItem deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsItem> findGoodsItemById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<GoodsItem> updateGoodsItem(@PathVariable("id") Integer id,
                                                     @Valid @RequestBody GoodsItemDto goodsItemDto) {
        return new ResponseEntity<>(service.update(dtoMapper.toEntity(goodsItemDto), id), HttpStatus.OK);
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
