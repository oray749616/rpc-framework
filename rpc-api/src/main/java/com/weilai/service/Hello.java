package com.weilai.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Hello
 * @Description: 测试用api实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hello implements Serializable {

    private Integer id;
    private String message;

}
