package model;

import java.io.Serializable;
import java.util.Objects;

public class Member implements Serializable {
    private int idMembers;
    private String name;
    private String gender;
    private String age;
    private String relation;
    private int Families_id;
    private int Limits_id;

    public Member(int idMembers, String name, String gender, String age, String relation, int families_id, int limits_id) {
        this.idMembers = idMembers;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.relation = relation;
        Families_id = families_id;
        Limits_id = limits_id;
    }

    public int getIdMembers() {
        return idMembers;
    }

    public void setIdMembers(int idMembers) {
        this.idMembers = idMembers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getFamilies_id() {
        return Families_id;
    }

    public void setFamilies_id(int families_id) {
        Families_id = families_id;
    }

    public int getLimits_id() {
        return Limits_id;
    }

    public void setLimits_id(int limits_id) {
        Limits_id = limits_id;
    }

    @Override
    public String toString() {
        return "Member{" +
                "idMembers=" + idMembers +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", relation='" + relation + '\'' +
                ", Families_id=" + Families_id +
                ", Limits_id=" + Limits_id +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Member member = (Member) obj;
        return idMembers == member.idMembers &&
                Families_id == member.Families_id &&
                Limits_id == member.Limits_id &&
                Objects.equals(name, member.name) &&
                Objects.equals(gender, member.gender) &&
                Objects.equals(age, member.age) &&
                Objects.equals(relation, member.relation);
    }
}
