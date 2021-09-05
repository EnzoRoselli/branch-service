package tesis.company.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branches")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;

}
