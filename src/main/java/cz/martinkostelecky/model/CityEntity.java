package cz.martinkostelecky.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * class of City entity
 */
@Entity
@Table(name = "city")
public class CityEntity {
    /**
     * class attributes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int code;
    private String name;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CityPartEntity> cityParts;
    /**
     * getters and setters
     */
    public long getId() {
        return id;
    }
    public void setId(long id) {
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
    public List<CityPartEntity> getCityParts() {
        return cityParts;
    }
    public void setCityParts(List<CityPartEntity> cityParts) {
        this.cityParts = cityParts;
    }
}
