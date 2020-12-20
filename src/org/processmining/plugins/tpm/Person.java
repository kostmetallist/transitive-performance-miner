package org.processmining.plugins.tpm;


public class Person {
    private Name name;
    private int age;

    public Person() {
        age = 0;
    }

    public Name getName() {
        return name;
    }

    void setName(Name name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    void setAge(int age) {
        this.age = age;
    }
}
