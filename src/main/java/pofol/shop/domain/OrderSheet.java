package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class OrderSheet {
    //----------필드 시작----------//
    @Id @GeneratedValue
    @Column(name = "ordersheet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;
    private Boolean isOrdered;
    //----------필드 끝----------//
}
