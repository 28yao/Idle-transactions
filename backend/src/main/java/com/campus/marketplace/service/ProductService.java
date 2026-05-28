package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.marketplace.dto.request.ProductCreateRequest;
import com.campus.marketplace.dto.request.ProductQuery;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.dto.response.ProductResponse;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.entity.ProductImage;
import com.campus.marketplace.entity.User;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.ProductImageMapper;
import com.campus.marketplace.mapper.ProductMapper;
import com.campus.marketplace.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final UserMapper userMapper;
    private final FavoriteService favoriteService;

    private static final int MAX_PUBLISHED = 20;
    private static final int MAX_IMAGES = 9;

    @Transactional
    public ProductResponse createProduct(Long sellerId, ProductCreateRequest request) {
        validateImages(request.getImages());

        // 检查在售上限
        if (request.getStatus() != null && request.getStatus() == 1) {
            LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
            w.eq(Product::getSellerId, sellerId).eq(Product::getStatus, 1);
            long count = productMapper.selectCount(w);
            if (count >= MAX_PUBLISHED) {
                throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(),
                        "最多同时发布" + MAX_PUBLISHED + "件商品");
            }
        }

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCondition(request.getCondition());
        product.setLocation(request.getLocation());
        // 校区：优先 request，否则取卖家校区
        if (request.getCampus() != null && !request.getCampus().isBlank()) {
            product.setCampus(request.getCampus());
        } else {
            User seller = userMapper.selectById(sellerId);
            if (seller != null) {
                product.setCampus(seller.getCampus());
            }
        }
        // status：0=草稿  1=在售
        product.setStatus(request.getStatus() == null ? 0 : request.getStatus());
        product.setViewCount(0);
        product.setFavCount(0);

        productMapper.insert(product);

        saveImages(product.getId(), request.getImages());

        return buildResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, Long sellerId, ProductCreateRequest request) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (!product.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        Integer current = product.getStatus();
        if (current != null && (current == 2 || current == 5)) {
            // 已售 / 违规下架 不允许编辑
            throw new BusinessException(ErrorCode.PRODUCT_STATUS_ERROR);
        }

        validateImages(request.getImages());

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCondition(request.getCondition());
        product.setLocation(request.getLocation());
        if (request.getCampus() != null && !request.getCampus().isBlank()) {
            product.setCampus(request.getCampus());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
        product.setRejectReason(null);

        productMapper.updateById(product);

        // 重置图片
        LambdaQueryWrapper<ProductImage> w = new LambdaQueryWrapper<>();
        w.eq(ProductImage::getProductId, productId);
        productImageMapper.delete(w);
        saveImages(productId, request.getImages());

        return buildResponse(product);
    }

    public ProductResponse getProductDetail(Long productId, Long userId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        // 浏览量 +1
        product.setViewCount((product.getViewCount() == null ? 0 : product.getViewCount()) + 1);
        productMapper.updateById(product);

        ProductResponse r = buildResponse(product);
        r.setFavorited(favoriteService.isFavorited(userId, productId));
        return r;
    }

    public PageResponse<ProductResponse> listProducts(ProductQuery query) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        // 默认只展示在售 (status=1)
        w.eq(Product::getStatus, 1);

        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String kw = query.getKeyword().trim();
            w.and(q -> q.like(Product::getTitle, kw).or().like(Product::getDescription, kw));
        }
        if (query.getCategory() != null && !query.getCategory().isBlank()) {
            w.eq(Product::getCategory, query.getCategory());
        }
        if (query.getCondition() != null && !query.getCondition().isBlank()) {
            w.eq(Product::getCondition, query.getCondition());
        }
        if (query.getCampus() != null && !query.getCampus().isBlank()) {
            w.eq(Product::getCampus, query.getCampus());
        }
        if (query.getMinPrice() != null) {
            w.ge(Product::getPrice, query.getMinPrice());
        }
        if (query.getMaxPrice() != null) {
            w.le(Product::getPrice, query.getMaxPrice());
        }

        String sort = query.getSort() == null ? "latest" : query.getSort();
        switch (sort) {
            case "price_asc" -> w.orderByAsc(Product::getPrice);
            case "price_desc" -> w.orderByDesc(Product::getPrice);
            case "popular" -> w.orderByDesc(Product::getViewCount);
            default -> w.orderByDesc(Product::getCreatedAt);
        }

        int page = query.getPage() == null || query.getPage() < 1 ? 1 : query.getPage();
        int size = query.getSize() == null || query.getSize() < 1 ? 20 : Math.min(query.getSize(), 100);
        IPage<Product> result = productMapper.selectPage(new Page<>(page, size), w);

        List<ProductResponse> items = new ArrayList<>();
        for (Product p : result.getRecords()) {
            items.add(buildResponse(p));
        }
        return PageResponse.of(items, result.getTotal(), size, page);
    }

    public List<ProductResponse> getRecommended(int limit) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.eq(Product::getStatus, 1).orderByDesc(Product::getViewCount).orderByDesc(Product::getCreatedAt).last("LIMIT " + Math.min(limit, 20));
        List<Product> list = productMapper.selectList(w);
        List<ProductResponse> result = new ArrayList<>();
        for (Product p : list) result.add(buildResponse(p));
        return result;
    }

    public List<ProductResponse> getLatest(int limit) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.eq(Product::getStatus, 1).orderByDesc(Product::getCreatedAt).last("LIMIT " + Math.min(limit, 20));
        List<Product> list = productMapper.selectList(w);
        List<ProductResponse> result = new ArrayList<>();
        for (Product p : list) result.add(buildResponse(p));
        return result;
    }

    @Transactional
    public void updateProductStatus(Long productId, Long sellerId, Integer newStatus) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (!product.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        Integer current = product.getStatus();

        // 已售/违规下架不允许改状态
        if (current != null && (current == 2 || current == 5)) {
            throw new BusinessException(ErrorCode.PRODUCT_STATUS_ERROR.getCode(), "当前状态不允许修改");
        }
        // 已售商品不能下架
        if (current != null && current == 2 && newStatus == 3) {
            throw new BusinessException(ErrorCode.PRODUCT_STATUS_ERROR.getCode(), "已售商品不能下架");
        }

        product.setStatus(newStatus);
        productMapper.updateById(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long sellerId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (!product.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        // 已售商品不能删除（有交易记录）
        if (product.getStatus() != null && product.getStatus() == 2) {
            throw new BusinessException(ErrorCode.PRODUCT_STATUS_ERROR.getCode(), "已售商品不能删除");
        }

        // 删除图片
        LambdaQueryWrapper<ProductImage> iw = new LambdaQueryWrapper<>();
        iw.eq(ProductImage::getProductId, productId);
        productImageMapper.delete(iw);

        productMapper.deleteById(productId);
    }

    public PageResponse<ProductResponse> listUserProducts(Long sellerId, Integer status, int page, int size) {
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.eq(Product::getSellerId, sellerId);
        if (status != null) {
            w.eq(Product::getStatus, status);
        }
        w.orderByDesc(Product::getCreatedAt);

        IPage<Product> result = productMapper.selectPage(new Page<>(page, size), w);
        List<ProductResponse> items = new ArrayList<>();
        for (Product p : result.getRecords()) {
            items.add(buildResponse(p));
        }
        return PageResponse.of(items, result.getTotal(), size, page);
    }

    private void validateImages(List<String> images) {
        if (images != null && images.size() > MAX_IMAGES) {
            throw new BusinessException(ErrorCode.PRODUCT_IMAGES_LIMIT);
        }
    }

    private void saveImages(Long productId, List<String> images) {
        if (images == null || images.isEmpty()) return;
        for (int i = 0; i < images.size(); i++) {
            ProductImage img = new ProductImage();
            img.setProductId(productId);
            img.setUrl(images.get(i));
            img.setSortOrder(i);
            img.setIsCover(i == 0 ? 1 : 0);
            productImageMapper.insert(img);
        }
    }

    private ProductResponse buildResponse(Product product) {
        ProductResponse r = ProductResponse.fromEntity(product);

        LambdaQueryWrapper<ProductImage> w = new LambdaQueryWrapper<>();
        w.eq(ProductImage::getProductId, product.getId()).orderByAsc(ProductImage::getSortOrder);
        List<ProductImage> imageList = productImageMapper.selectList(w);
        List<String> urls = new ArrayList<>();
        for (ProductImage img : imageList) {
            urls.add(img.getUrl());
            if (img.getIsCover() != null && img.getIsCover() == 1) {
                r.setCoverImage(img.getUrl());
            }
        }
        if (r.getCoverImage() == null && !urls.isEmpty()) {
            r.setCoverImage(urls.get(0));
        }
        r.setImages(urls);

        User seller = userMapper.selectById(product.getSellerId());
        if (seller != null) {
            r.setSellerNickname(seller.getNickname());
            r.setSellerAvatar(seller.getAvatar());
            r.setSellerVerifyStatus(seller.getVerifyStatus());
        }
        return r;
    }
}
