package org.timattt.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "result")
@Data
public class Result {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "result_data", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private ResultData resultData;

}
