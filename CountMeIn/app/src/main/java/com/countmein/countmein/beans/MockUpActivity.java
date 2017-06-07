package com.countmein.countmein.beans;

import java.io.Serializable;

/**
 * Created by Home on 6/6/2017.
 */

public class MockUpActivity extends BaseModel implements Serializable{
    public String name;
    public String description;

    public MockUpActivity(String id,String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public MockUpActivity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockUpActivity that = (MockUpActivity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override

    public String toString() {
        return "MockUpActivity{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
