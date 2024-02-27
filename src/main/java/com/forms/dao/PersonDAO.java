package com.forms.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import com.forms.models.Person;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
@PropertySource("classpath:settings.properties")
public class PersonDAO {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, age, email) VALUES( ?, ?, ?)", person.getName(), person.getAge(),
                person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?", updatedPerson.getName(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }



    //Batch

    public void tesMultipleUpdate(){
        List<Person> peoles = create1000Peuple();
        long before = System.currentTimeMillis();
        for(Person person: peoles){
            save(person);
        }
        long after = System.currentTimeMillis();
        System.out.println(after-before);
    }

    public void testBatchUpdate(){
        List<Person> peoles = create1000Peuple();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO Person(name, age, email) VALUES( ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, peoles.get(i).getName());
                preparedStatement.setInt(2, peoles.get(i).getAge());
                preparedStatement.setString(3, peoles.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return peoles.size();
            }
        });
        long after = System.currentTimeMillis();
        System.out.println(after-before);
    }

    private List<Person> create1000Peuple(){
        List<Person> peoples = new ArrayList<>();
        for(int i = 0 ; i< 1000; i++){
            peoples.add(new Person(i, "Name"+i, 44, "email"+i+"@mail.ru"));
        }
        return peoples;
    }

}
