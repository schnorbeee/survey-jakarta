package com.dynata.surveyhw.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "participation",
        uniqueConstraints = @UniqueConstraint(columnNames = { "member_id", "survey_id", "status_id" })
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable = false)
    private Status status;

    private Integer length;
}
