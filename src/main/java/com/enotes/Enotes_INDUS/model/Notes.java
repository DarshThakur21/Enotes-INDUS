package com.enotes.Enotes_INDUS.model;

import com.enotes.Enotes_INDUS.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notes extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    private String title;
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    private FileDetails fileDetails;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_on")
    private Date deletedOn;
//    private Integer userId;


}
