package com.weilai.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description: TODO
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String userName;
    private Boolean sex;
}
