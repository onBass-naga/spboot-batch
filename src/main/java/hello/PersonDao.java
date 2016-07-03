package hello;

import org.seasar.doma.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Dao
@AnnotateWith(annotations={
        @Annotation(target= AnnotationTarget.CLASS, type=Repository.class),
        @Annotation(target=AnnotationTarget.CONSTRUCTOR, type=Autowired.class)
})
public interface PersonDao {

    @Select
    public List<Person> findAll();
}
