<h1 style="text-align:center;">Service desk</h1>

<p><br></p>

<div style="text-align: center; margin-bottom: 1.5rem;">
    <img src="/images/icon.ico"
         alt="MyStock logo"
         style="width: 150px; height: auto;" />
</div>

---  

Данное веб-приложение предназначено для ...  

We configured the relationships to the Student and Course classes as @ManyToOne. We could do this because with the new entity we structurally decomposed the many-to-many relationship to two many-to-one relationships.

Why were we able to do this? If we inspect the tables closely in the previous case, we can see that it contained two many-to-one relationships. In other words, there isn’t any many-to-many relationship in an RDBMS. We call the structures we create with join tables many-to-many relationships because that’s what we model.

Besides, it’s more clear if we talk about many-to-many relationships because that’s our intention. Meanwhile, a join table is just an implementation detail; we don’t really care about it.

Moreover, this solution has an additional feature we haven’t mentioned yet. The simple many-to-many solution creates a relationship between two entities. Therefore, we cannot expand the relationship to more entities. But we don’t have this limit in this solution: we can model relationships between any number of entity types.

For example, when multiple teachers can teach a course, students can rate how a specific teacher teaches a specific course. That way, a rating would be a relationship between three entities: a student, a course and a teacher.
