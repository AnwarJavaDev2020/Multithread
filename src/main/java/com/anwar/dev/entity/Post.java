/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Developer1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Post implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String title;
    private String body;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,targetEntity = Comment.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="pc_fk",referencedColumnName = "id")
    private Collection<Comment> comments=new LinkedHashSet<Comment>();
}
