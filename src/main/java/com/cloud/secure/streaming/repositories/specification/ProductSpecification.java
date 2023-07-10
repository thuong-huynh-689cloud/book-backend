package com.cloud.secure.streaming.repositories.specification;

import com.cloud.secure.streaming.common.enums.Status;
import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.entities.Category;
import com.cloud.secure.streaming.entities.Product;
import com.cloud.secure.streaming.entities.ProductCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification {

    public Specification<Product> doFilterProduct(
            String searchKey,
            Boolean isAsc,
            String sortField,
            List<String> categoryIds ,
            Double fromPrice,
            Double toPrice

    ) {
        return (Root<Product> productRoot, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            cq.distinct(true);

            List<Predicate> predicates = new ArrayList<>();


            predicates.add(cb.equal(productRoot.get("status"), Status.ACTIVE));

            if (searchKey != null && !searchKey.trim().isEmpty()) {
                predicates.add(cb.like(productRoot.get("name"),"%"+searchKey+"%"));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                Root<ProductCategory> productCategoryRoot = cq.from(ProductCategory.class);
                predicates.add(cb.equal(productRoot.get("id"), productCategoryRoot.get("productCategoryId").get("productId")));
                Root<Category> categoryRoot = cq.from(  Category.class);
                predicates.add(cb.equal(categoryRoot.get("id"), productCategoryRoot.get("productCategoryId").get("categoryId")));
                predicates.add(categoryRoot.get("id").in(categoryIds));
            }

            if(fromPrice != null && toPrice != null && fromPrice > 0 && toPrice > 0) {
                predicates.add(cb.lessThanOrEqualTo(productRoot.get("salePrice"),toPrice));
                predicates.add(cb.greaterThanOrEqualTo(productRoot.get("salePrice"),fromPrice));
//                predicates.add(cb.between(productRoot.get("salePrice"),fromPrice,toPrice));
            }else if (toPrice != null && toPrice > 0 && fromPrice == null) {
                predicates.add(cb.lessThanOrEqualTo(productRoot.get("salePrice"),toPrice));
            }else if(fromPrice != null && fromPrice > 0 && toPrice == null) {
                predicates.add(cb.greaterThanOrEqualTo(productRoot.get("salePrice"),fromPrice));
            }

            Path orderClause;
            switch (sortField) {
                case Constant.SORT_BY_CI:
                    orderClause = productRoot.get("name");
                    break;
                default:
                    orderClause = productRoot.get("salePrice");
                    break;
            }

            if (isAsc) {
                cq.orderBy(cb.asc(orderClause));
            } else {
                cq.orderBy(cb.desc(orderClause));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
