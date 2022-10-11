package com.aronalvarenga.pdv.service;

import com.aronalvarenga.pdv.dto.ProductDTO;
import com.aronalvarenga.pdv.dto.ProductInfoDTO;
import com.aronalvarenga.pdv.dto.SaleDTO;
import com.aronalvarenga.pdv.dto.SaleInfoDTO;
import com.aronalvarenga.pdv.entity.ItemSale;
import com.aronalvarenga.pdv.entity.Product;
import com.aronalvarenga.pdv.entity.Sale;
import com.aronalvarenga.pdv.entity.User;
import com.aronalvarenga.pdv.exceptions.InvalidOperationException;
import com.aronalvarenga.pdv.exceptions.NoItemException;
import com.aronalvarenga.pdv.repository.ItemSaleRepository;
import com.aronalvarenga.pdv.repository.ProductRepository;
import com.aronalvarenga.pdv.repository.SaleRepository;
import com.aronalvarenga.pdv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;

    /*public  SaleService(@Autowired UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }*/

    public List<SaleInfoDTO> findAll() {
        return saleRepository.findAll().stream().map(sale -> getSaleInfo(sale)).collect(Collectors.toList());
    }

    private SaleInfoDTO getSaleInfo(Sale sale) {
        SaleInfoDTO saleInfoDTO = new SaleInfoDTO();
        saleInfoDTO.setUser(sale.getUser().getName());
        saleInfoDTO.setDatetime(sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        saleInfoDTO.setProducts(getProductInfo(sale.getItems()));

        return saleInfoDTO;
    }

    private List<ProductInfoDTO> getProductInfo(List<ItemSale> items) {
        return items.stream().map(item -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            productInfoDTO.setId(item.getId());
            productInfoDTO.setDescription(item.getProduct().getDescription());
            productInfoDTO.setQuantity(item.getQuantity());
            return productInfoDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public long save(SaleDTO sale) {

        User user = userRepository.findById(sale.getUserid()).orElseThrow(() -> new NoItemException("User not found!"));

        Sale newSale = new Sale();
        newSale.setUser(user);
        newSale.setDate(LocalDate.now());
        List<ItemSale> items = getItemSale(sale.getItems());

        newSale = saleRepository.save(newSale);

        saveItemSale(items, newSale);

        return newSale.getId();

    }

    private void saveItemSale(List<ItemSale> items, Sale newSale) {
        for(ItemSale item: items) {
            item.setSale(newSale);
            itemSaleRepository.save(item);
        }
    }

    private List<ItemSale> getItemSale(List<ProductDTO> products) {

        if(products.isEmpty()) {
            throw new InvalidOperationException("Cannot add a sale without items!");
        }

        return products.stream().map(item -> {
            Product product = productRepository.getReferenceById(item.getProductid());

            ItemSale itemSale = new ItemSale();
            itemSale.setProduct(product);
            itemSale.setQuantity(item.getQuantity());

            if(product.getQuantity() == 0) {
                throw new NoItemException("No product in stock!");
            } else if(product.getQuantity() < item.getQuantity()) {
                throw new InvalidOperationException(String.format("The quantity of items on sale (%s) is greater than the quantity available in stock (%s)!", item.getQuantity(), product.getQuantity()));
            }

            int total = product.getQuantity() - item.getQuantity();
            product.setQuantity(total);
            productRepository.save(product);

            return itemSale;

        }).collect(Collectors.toList());

    }

    public SaleInfoDTO getById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new NoItemException("Sale not found!"));
        return getSaleInfo(sale);
    }
}
