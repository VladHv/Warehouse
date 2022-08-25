package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String firstName;
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
}
