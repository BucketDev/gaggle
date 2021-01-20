package com.bucketdev.gaggle.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rodrigo.loyola
 * @param <Key> type of the identifier
 */
@Getter @Setter
public abstract class CoreDTO<Key> implements Serializable, Comparable<CoreDTO> {

    @ApiModelProperty(value = "Database recorded identifier", required = true, example = "1")
    protected Key id;

    @Override
    public int compareTo(CoreDTO o) {
        return Math.toIntExact(this.getId().hashCode() - o.getId().hashCode());
    }

}
