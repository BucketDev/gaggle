package com.bucketdev.gaggle.profile.dto;

import com.bucketdev.gaggle.core.dto.CoreDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rodrigo.loyola
 */
@Getter @Setter
public class GaggleUserDTO extends CoreDTO<Long> {
    @ApiModelProperty(value = "value to display on the app", example = "username")
    private String userName;
    @ApiModelProperty(value = "email of the user", required = true, example = "example@company.com")
    private String email;
    @ApiModelProperty(value = "URL to the profile photo image", example = "https://example.com/path-to-image.extension")
    private String photoURL;
}
