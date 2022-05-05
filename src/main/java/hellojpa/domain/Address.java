package hellojpa.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {


    private String city;
    private String street;
    @Column(nullable = true)
    private int zip;


    public Address() {
    }

    public Address(String city, String street, int zip) {
        this.city = city;
        this.street = street;
        this.zip = zip;
    }


    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getZip() {
        return zip;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zip=" + zip +
                '}';
    }
}
