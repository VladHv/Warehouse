package ua.foxminded.herasimov.warehouse.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The unique id of the person", example = "1")
    protected Integer id;

    @NotBlank(message = "First name required")
    @Size(min = 2, max = 250, message = "Length should be from 2 to 250")
    @ApiModelProperty(notes = "The person's first name", example = "Kevin")
    protected String firstName;

    @NotBlank(message = "Last name required")
    @Size(min = 2, max = 250, message = "Length should be from 2 to 250")
    @ApiModelProperty(notes = "The person's last name", example = "Eddy")
    protected String lastName;

    protected Person() {
    }

    protected abstract static class ABuilder<T extends Person, B extends ABuilder<T, B>> {
        protected T object;
        protected B thisObject;

        protected abstract T getObject();

        protected abstract B thisObject();

        protected ABuilder() {
            object = getObject();
            thisObject = thisObject();
        }

        public B withId(Integer id) {
            object.id = id;
            return thisObject;
        }

        public B withFirstName(String firstName) {
            object.firstName = firstName;
            return thisObject;
        }


        public B withLastName(String lastName) {
            object.lastName = lastName;
            return thisObject;
        }

        public T build() {
            return object;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
