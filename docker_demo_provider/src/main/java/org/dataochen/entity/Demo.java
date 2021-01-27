package org.dataochen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author Mybatis Generator
 * @date 2021-01-26
 */
@Data
public class Demo implements Serializable{
    private static final long serialVersionUID = 7600674844897215214L;
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Date createdDate;
}