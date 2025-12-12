package com.household.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "hh_common_code")
@Getter
@Setter
@NoArgsConstructor
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_pk")
    private Integer codePk;

    @Column(name = "group_code", nullable = false, length = 50)
    private String groupCode;

    @Column(name = "code_value", nullable = false, length = 20)
    private String codeValue;

    @Column(name = "code_name", nullable = false, length = 50)
    private String codeName;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(useYn == null) useYn = "Y";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
