package com.bucketdev.gaggle.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rodrigo.loyola
 */
@Component
public class PaginationUtil {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public Pageable getPageableData(HttpServletRequest request, Pageable pageable) {
        int pageSize = 10;
        int pageNumber = 0;
        Sort sort = Sort.unsorted();
        try {
            if (null != request.getParameter("pageSize")) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            } else if (null != request.getHeader("pageSize")) {
                pageSize = Integer.parseInt(request.getHeader("pageSize"));
            }
        } catch (Exception ex) {
            log.warn("Unable to retrieve PageSize from request");
        }

        try {
            if (null != request.getParameter("pageNumber")) {
                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            } else if (null != request.getHeader("pageNumber")) {
                pageNumber = Integer.parseInt(request.getHeader("pageNumber"));
            }
        } catch (Exception ex) {
            log.warn("Unable to retrieve pageNumber from request");
        }

        try {
            if (null != request.getParameter("sort")) {
                List<Sort.Order> sorts = new ArrayList<>();
                for (String sortRawEntry : request.getParameterValues("sort")) {
                    String[] sortEntry = sortRawEntry.split(",");
                    String sortFieldName = sortEntry[0];
                    String sortDirection = sortEntry.length == 1 ?
                        "asc" :
                        sortEntry[1].toLowerCase().contains("desc") ? "desc" : "asc";
                    Sort.Order order =
                        new Sort.Order(
                            sortDirection.equals("desc") ?
                                Sort.Direction.DESC : Sort.Direction.ASC,
                            sortFieldName);
                    sorts.add(order);
                }
                sort = Sort.by(sorts);
            }
        } catch (Exception ex) {
            log.warn("Unable to retrieve sort from request");
        }

        try {
            if ((null != request.getParameter("paged")) &&
                (!Boolean.parseBoolean(request.getParameter("paged")))) {
                pageNumber = 0;
                pageSize = Integer.MAX_VALUE;
            }
        } catch (Exception ex) {
            log.warn("Unable to gently manage the page request");
        }
        return PageRequest.of(pageNumber, pageSize, sort);
    }

}
