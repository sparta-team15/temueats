package com.sparta.temueats.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_REPORT")
public class P_report {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID reportId;

    @Column(nullable = true)
    private String reportDetail;

    @Column(nullable = true)
    private boolean resolvedYn;

    @Column
    private LocalDateTime resolvedDate;


}
