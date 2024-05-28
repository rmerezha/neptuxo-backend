package space.neptuxo.util;

import org.junit.jupiter.api.Test;
import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.ProductType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLQueryBuilderTest {

    @Test
    public void buildQueryWithAllFilters() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                100,
                500,
                5,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price >= 100 AND price <= 500 AND id > 5 AND name ILIKE '%phone%' LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullLimit() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                null,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                100,
                500,
                5,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price >= 100 AND price <= 500 AND id > 5 AND name ILIKE '%phone%'";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullTypes() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                null,
                100,
                500,
                5,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE price >= 100 AND price <= 500 AND id > 5 AND name ILIKE '%phone%' LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullPriceFrom() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                null,
                500,
                5,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price <= 500 AND id > 5 AND name ILIKE '%phone%' LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullPriceTo() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                100,
                null,
                5,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price >= 100 AND id > 5 AND name ILIKE '%phone%' LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullLastIndex() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                100,
                500,
                null,
                "phone"
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price >= 100 AND price <= 500 AND name ILIKE '%phone%' LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithNullSearchQuery() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(
                10,
                List.of(ProductType.ELECTRONICS, ProductType.ANIMALS),
                100,
                500,
                5,
                null
        );
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products WHERE (type = 'ELECTRONICS' OR type = 'ANIMALS') AND price >= 100 AND price <= 500 AND id > 5 LIMIT 10";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void buildQueryWithAllNull() {
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        ProductFilterDto filter = new ProductFilterDto(null, null, null, null, null, null);
        String baseQuery = "SELECT * FROM products";
        String expectedQuery = "SELECT * FROM products";

        String actualQuery = queryBuilder.buildQuery(baseQuery, filter);

        assertEquals(expectedQuery, actualQuery);
    }
}