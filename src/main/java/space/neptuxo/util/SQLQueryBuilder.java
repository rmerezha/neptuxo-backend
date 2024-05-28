package space.neptuxo.util;

import space.neptuxo.dto.ProductFilterDto;
import space.neptuxo.entity.ProductType;

import java.util.StringJoiner;

public class SQLQueryBuilder {

    private static final int WHERE_CLAUSE_LENGTH = " WHERE ".length();

    public String buildQuery(String baseQuery, ProductFilterDto filter) {

        StringBuilder queryBuilder = new StringBuilder(baseQuery);

        StringJoiner whereClause = new StringJoiner(" AND ", " WHERE ", "");

        if (filter.types() != null && !filter.types().isEmpty()) {
            StringJoiner typeConditions = new StringJoiner(" OR ", "(", ")");
            for (ProductType type : filter.types()) {
                typeConditions.add("type = '" + type.name() + "'");
            }
            whereClause.add(typeConditions.toString());
        }

        if (filter.priceFrom() != null) {
            whereClause.add("price >= " + filter.priceFrom());
        }

        if (filter.priceTo() != null) {
            whereClause.add("price <= " + filter.priceTo());
        }

        if (filter.lastIndex() != null) {
            whereClause.add("id > " + filter.lastIndex());
        }

        if (filter.searchQuery() != null && !filter.searchQuery().isEmpty()) {
            whereClause.add("name ILIKE '%" + filter.searchQuery().replaceAll(";", " ") + "%'");
        }

        if (whereClause.length() > WHERE_CLAUSE_LENGTH) {
            queryBuilder.append(whereClause);
        }

        if (filter.limit() != null) {
            queryBuilder.append(" LIMIT ").append(filter.limit());
        }

        return queryBuilder.toString();
    }
}