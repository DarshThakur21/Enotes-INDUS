package com.enotes.Enotes_INDUS.model;

import com.enotes.Enotes_INDUS.model.enums.Status;
import com.enotes.Enotes_INDUS.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Todo extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;



//    public enum Status{
//
//    }


}
