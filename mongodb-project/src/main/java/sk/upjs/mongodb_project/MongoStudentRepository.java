package sk.upjs.mongodb_project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MongoStudentRepository extends MongoRepository<MongoStudent, Long> {
    @Query(value="{}", fields="{meno : 1, priezvisko : 1, _id : 0}")
    public List<MenoAPriezvisko> findMenoAPriezviskoBySkratkaakadtitul(String titul);

    public List<MongoStudent> findMongoStudentByZoznamStudii(MongoStudium studium);

    public List<MongoStudent> findByPriezvisko(String priezvisko);

    @Query(value = "{ 'zoznamStudii.studijnyProgram' : ?0, 'zoznamStudii.zaciatokStudia' : { $lte: ?1} , 'zoznamStudii.koniecStudia': { $gte: ?1} }")
    public List<MongoStudent> findMongoStudentByStudijnyProgramAndYear(MongoStudijnyProgram program, Date year);
}
