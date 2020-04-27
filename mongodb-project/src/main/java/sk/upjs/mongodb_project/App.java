package sk.upjs.mongodb_project;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				RepositoryConfiguration.class);

		MongoStudentService service = context.getBean(MongoStudentService.class);

		service.initDatabase();
		
		System.out.println("DB initiated");

		prvaUloha(service);

		// indexy su na zaciatku vytvorene kvôli anotácii v MongoStudium
		service.printIndexes();

		long durationIndexed = druhaUloha(service);

		service.dropIndexes();
		service.printIndexes();

		long durationNotIndexed = druhaUloha(service);

		System.out.println("Trvanie bez indexu: " + durationNotIndexed);
		System.out.println("Trvanie s indexom: " + durationIndexed);

		service.deleteAll();
		context.close();

	}

	/**
	 * Zobrazte všetkých študentov, ktorí študujú v danom roku daný študijný
	 * program.
	 * 
	 * @param service
	 */
	private static long druhaUloha(MongoStudentService service) {
		MongoStudent student = service.findAll().get(0);
		List<MongoStudium> st = student.getZoznamStudii();
		MongoStudium stu = st.get(0);
		MongoStudijnyProgram p = stu.getStudijnyProgram();
		int year = 2000;
		System.out.println("Úloha 2: Študenti programu " + p.getSkratka() + " v roku " + year);
		long duration = service.findByStudijnyProgramAndYear(p, 2000);
		System.out.println(
				"------------------------------------------------------------------------------------------\n\n");
		return duration;
	}

	/**
	 * Vytvorte repozitár triedy Student pomocou CrudRepository projektu Spring data
	 * (študent má uložené aj štúdium a študijný program). Otestujte jeho základnú
	 * funkcionalitu a potom rozšírte možnosti repozitára o získania iba mien a
	 * priezvisk (projekcia) na základe akademického titulu.
	 * 
	 * @param service
	 */
	private static void prvaUloha(MongoStudentService service) {
		// Uloha1 : Mena a priezviska podla titulu
		String titul = "Mgr.";
		System.out.println("Uloha 1 - mená a priezviská podľa titulu: " + titul);
		service.printMenoAPriezviskoByTitul(titul);
		System.out.println(
				"------------------------------------------------------------------------------------------\n\n");
	}
}
