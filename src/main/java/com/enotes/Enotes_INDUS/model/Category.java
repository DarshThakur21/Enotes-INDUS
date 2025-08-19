package com.enotes.Enotes_INDUS.model;


//sql
//id int primary key  auto_increment ,
//    name varchar(50) not null,
//    is_active bit(1) default b'0' not null,
//    is_deleted bit(1) default b'0' not null ,
//    created_by int default 0  not null,
//    created_on timestamp DEFAULT CURRENT_TIMESTAMP not null,
//    updated_by int default 0 not null,
//    updated_on timestamp default null



import com.enotes.Enotes_INDUS.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;



@Data
@Entity
@Getter
@Setter
@AllArgsConstructor


public class Category extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private String name;
    public Category(){}



}
