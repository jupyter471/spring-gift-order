package gift.controller;

import gift.dto.*;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;


    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        productService.addProduct(requestDto);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK, "상품이 성공적으로 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "id", value = "criteria") String criteria
    ) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, criteria));
        List<ProductResponseDto> responses = productService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long id) {
        ProductResponseDto response = productService.findProduct(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> editProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductChangeRequestDto request) {
        ProductResponseDto edited = productService.editProduct(id, request);
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<ApiResponse> saveOption(@PathVariable("productId") Long productId,
                                                  @RequestBody OptionRequestDto request) {
        optionService.saveOption(productId, request);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK, "옵션이 성공적으로 등록되었습니다."));
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptions(@PathVariable("productId") Long productId) {
        var result = optionService.getOptions(productId);
        return ResponseEntity.ok(result);
    }
}