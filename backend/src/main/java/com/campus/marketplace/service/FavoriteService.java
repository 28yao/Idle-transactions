package com.campus.marketplace.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.marketplace.entity.Favorite;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import com.campus.marketplace.mapper.FavoriteMapper;
import com.campus.marketplace.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;

    private static final int MAX_FAVORITES = 100;

    @Transactional
    public void addFavorite(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (product.getSellerId().equals(userId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "不能收藏自己的商品");
        }

        LambdaQueryWrapper<Favorite> existsQ = new LambdaQueryWrapper<>();
        existsQ.eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId);
        if (favoriteMapper.selectCount(existsQ) > 0) {
            return; // 幂等
        }

        LambdaQueryWrapper<Favorite> countQ = new LambdaQueryWrapper<>();
        countQ.eq(Favorite::getUserId, userId);
        if (favoriteMapper.selectCount(countQ) >= MAX_FAVORITES) {
            throw new BusinessException(ErrorCode.BAD_REQUEST.getCode(), "收藏数量已达上限");
        }

        Favorite f = new Favorite();
        f.setUserId(userId);
        f.setProductId(productId);
        favoriteMapper.insert(f);

        product.setFavCount((product.getFavCount() == null ? 0 : product.getFavCount()) + 1);
        productMapper.updateById(product);
    }

    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> w = new LambdaQueryWrapper<>();
        w.eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId);
        int deleted = favoriteMapper.delete(w);
        if (deleted > 0) {
            Product product = productMapper.selectById(productId);
            if (product != null) {
                int next = (product.getFavCount() == null ? 0 : product.getFavCount()) - 1;
                product.setFavCount(Math.max(next, 0));
                productMapper.updateById(product);
            }
        }
    }

    public boolean isFavorited(Long userId, Long productId) {
        if (userId == null || productId == null) return false;
        LambdaQueryWrapper<Favorite> w = new LambdaQueryWrapper<>();
        w.eq(Favorite::getUserId, userId).eq(Favorite::getProductId, productId);
        return favoriteMapper.selectCount(w) > 0;
    }
}
