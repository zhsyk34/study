package com.cat.model;

/**
 * Created by Archimedes on 2016/7/11.
 */
public class Info {

    private int id;//索引,自增

    private String job;//职位

    private String company;

    private String link;//链接

    private String salary;//薪资

    private String place;//地点

    public Info(int id, String company, String job, String link, String place, String salary) {
        this.id = id;
        this.company = company;
        this.job = job;
        this.link = link;
        this.place = place;
        this.salary = salary;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Info{" +
                "company='" + company + '\'' +
                ", id=" + id +
                ", job='" + job + '\'' +
                ", link='" + link + '\'' +
                ", salary='" + salary + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

    public void printf() {
        System.out.printf("%3d : %-20.20s %-18.18s %-20.20s %-60s\n", id, job, company, salary, link);
    }
}
