package authentication.records;

import java.util.List;

public record PagedResult<T>(int totalItem, int pageSize, int currentPage, int totalPage, List<T> result) {
}
