package com.aronalvarenga.pdv.controller;

import com.aronalvarenga.pdv.dto.ResponseDTO;
import com.aronalvarenga.pdv.dto.SaleDTO;
import com.aronalvarenga.pdv.dto.SaleInfoDTO;
import com.aronalvarenga.pdv.exceptions.InvalidOperationException;
import com.aronalvarenga.pdv.exceptions.NoItemException;
import com.aronalvarenga.pdv.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController {

    private SaleService saleService;

    public SaleController(@Autowired SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping()
    public ResponseEntity getAll() {
        return new ResponseEntity<>(new ResponseDTO<List<SaleInfoDTO>>("", saleService.findAll()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity getById(@PathVariable Long id){
        try{
            return new ResponseEntity(new ResponseDTO<>("", saleService.getById(id)), HttpStatus.OK);
        } catch (NoItemException | InvalidOperationException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody SaleDTO saleDTO) {
        try {
            long id = saleService.save(saleDTO);
            return new ResponseEntity(new ResponseDTO<>("Sale successfully added!", id), HttpStatus.CREATED);
        } catch (NoItemException | InvalidOperationException e){
            return new ResponseEntity(new ResponseDTO<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity(new ResponseDTO<>(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
