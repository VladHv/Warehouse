package ua.foxminded.herasimov.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.service.GoodsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService service;

    @Autowired
    public GoodsController(GoodsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Goods>> getAllGoods() {
        List<Goods> goods = service.findAll();
        return goods != null && !goods.isEmpty()
               ? new ResponseEntity<>(goods, HttpStatus.OK)
               : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createGoods(@Valid @RequestBody Goods goods) {
        service.create(goods);
//        return ResponseEntity.ok("Goods is valid");
        return new ResponseEntity<>("Goods is valid", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteGoods(@PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>("Goods deleted successfully", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Goods> findGoodsById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goods> updateGoods(@PathVariable(name = "id") Integer id, @Valid @RequestBody Goods goods) {
        return new ResponseEntity<>(service.update(goods, id), HttpStatus.OK);
    }

}
