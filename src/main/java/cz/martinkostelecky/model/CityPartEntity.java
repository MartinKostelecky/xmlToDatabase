package cz.martinkostelecky.model;

import jakarta.persistence.*;
/**
 * class of City part entity
 */
@Entity
@Table(name = "city_parts")
public class CityPartEntity {
    /**
     * class attributes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int code;
    int cityCode;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;
    /**
     * getters and setters
     */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CityEntity getCity() {
        return city;
    }
    public void setCity(CityEntity city) {
        this.city = city;
    }
    public int getCityCode() { return cityCode; }
    public void setCityCode(int cityCode) { this.cityCode = cityCode; }
}
