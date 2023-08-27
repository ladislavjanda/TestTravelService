package cz.istep.javatest.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class JavaScriptFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 30)
    @NotEmpty(message = "Name is mandatory")
    private String name;

    @Size(min = 0, max = 30)
    private String versionNumber;

    private int hypeLevel;

    public JavaScriptFramework() {
    }

    public JavaScriptFramework(String name) {
        this.name = name;
    }

    public JavaScriptFramework(String name, String versionNumber, int hypeLevel) {
        this.name = name;
        this.versionNumber = versionNumber;
        this.hypeLevel = hypeLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(int hypeLevel) {
        this.hypeLevel = hypeLevel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaScriptFramework that = (JavaScriptFramework) o;
        return hypeLevel == that.hypeLevel && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(versionNumber, that.versionNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, versionNumber, hypeLevel);
    }

    @Override
    public String toString() {
        return "JavaScriptFramework{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", hypeLevel=" + hypeLevel +
                '}';
    }
}
