package org.einclusion.model;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.DynamicUpdate;

import static org.einclusion.model.ModelManager.*;

@Entity
@Table(name="Student")
@DynamicUpdate(value = true)
public class Student implements Serializable {
	private static final Logger LOG = Logger.getLogger(Student.class);
	private static final long serialVersionUID = 10001L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	Long ID; 			// system generated unique identificator
	String PHONE; 		// students phone number
	String NAME; 		// student name and surname
	String TOPIC; 		// subject (topic)
	Double DS; 			// digital skills (average)
	Double ELE; 		// e-learning environment (average)
	Double ELM; 		// e-learning materials (average)
	Double IWS; 		// instructor willing to share knowledge
	Double SAL; 		// student ability learn
	Double SWL; 		// student wiling to learn (average)
	Double KLAL;		// knowledge after learning
	Double KLBL; 		// knowledge before learning
	Integer OU; 		// observed usage
	Double PU; 			// predicted usage
	Double PUOU; 		// combination of predicted usage and observed usage (ou + pu)
	Integer M1;			// m1 value
	Double M2; 			// m2 value
	Double KFA;			// knowledge flow acceleration
	Double M3;			// m3 value
	String RELIABILITY;	// reliability of prediction model
	Date SUBMITDATE; 	// date (when the survey was completed)
	
	public Long getId(){ return ID; }
	
	public void setPhone(String PHONE){ this.PHONE = (PHONE != null) ? PHONE : "-1"; }
	public String getPhone(){ return PHONE; }
	
	public void setName(String NAME){ this.NAME = (NAME != null) ? NAME : "-1"; }
	public String getName(){ return NAME; }
	
	public void setTopic(String TOPIC){ this.TOPIC = (TOPIC != null) ? TOPIC : "-1"; }
	public String getTopic(){ return TOPIC; }
	
	public void setDS(Double DS){ this.DS = ( DS >= 1 && DS <= 5 ) ? DS : -1; }
	public Double getDS(){ return DS; }
	
	public void setELE(Double ELE){ this.ELE = ( ELE >= 1 && ELE <= 5) ? ELE : -1;  }
	public Double getELE(){ return ELE; }
	
	public void setELM(Double ELM){ this.ELM = ( ELM >= 1 && ELM <= 5) ? ELM : -1; }
	public Double getELM(){ return ELM; }
	
	public void setIWS(Double IWS){ this.IWS = ( IWS >= 1 && IWS <= 5) ? IWS : -1;  }
	public Double getIWS(){ return IWS; }
	
	public void setSAL(Double SAL){ this.SAL = ( SAL >= 1 && SAL <= 5) ? SAL : -1; }
	public Double getSAL(){ return SAL; }	
	
	public void setSWL(Double SWL){ this.SWL = ( SWL >= 1 && SWL <=5 ) ? SWL : -1; }
	public Double getSWL(){ return SWL; }
	
	public void setKLAL(Double KLAL){ this.KLAL = ( KLAL >= 1 && KLAL <= 5) ? KLAL : -1; }
	public Double getKLAL(){ return KLAL; }
	
	public void setKLBL(Double KLBL){ this.KLBL = ( KLBL >= 1 && KLBL <= 5) ? KLBL : -1; }
	public Double getKLBL(){ return KLBL; }
	
	public void setOU(Integer OU){ this.OU = ( OU >= 0 && OU <= 2 ) ? OU : -1; }
	public Integer getOU(){ return OU; }
	
	public void setPU(Double PU){ this.PU = ( PU >= 1 && PU <= 5) ? PU : -1; }
	public Double getPU(){ return PU; }
	
	public void setPUOU(){
		Double tmp = getOU() + getPU();
		this.PUOU = ( tmp >= 1 && tmp <= 7 ) ? tmp : -1;
	}
	public Double getPUOU(){ return PUOU; }
	
	public void setM1(int M1){ this.M1 = (M1 >= 0 && M1 <= 2) ? M1 : 0; }
	public int getM1(){ return M1; }

	public void setM2(Double M2){ this.M2 = (M2 >= 0 && M2 <= 100) ? M2 : 0; }
	public Double getM2(){ return M2; }

	public void setKFA(Double KFA){ this.KFA = (KFA > 0) ? KFA : 0; }
	public Double getKFA(){ return KFA; }

	public void setM3(Double M3){ this.M3 = (M3 >= 0 && M3 <= 100) ? M3 : 0; }
	public Double getM3(){ return M3; }
	
	public void setReliability(String RELIABILITY){ this.RELIABILITY = RELIABILITY; }
	public String getRealibility(){ return RELIABILITY; }
	
	public void setDate(Date SUBMITDATE){ this.SUBMITDATE = SUBMITDATE;}
	public Date getDate(){ return SUBMITDATE; }
	
	public static List<Student> getStudent() {
		List<Student> tmp = new LinkedList<Student>();
		try {
			TypedQuery<Student> query = entityManager.createQuery(
					"FROM Student", Student.class);

			List<Student> students = query.getResultList();
			return students;

		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
		return tmp;
	}
	
	/**
	 * 	Returns a list of students in a topic
	 * @param topic - name of topic (String)
	 * @return List(Students)
	 */
	public static List<Student> getStudents(String topic) {
		List<Student> tmp = new LinkedList<Student>();
		try {
			TypedQuery<Student> query = entityManager.createQuery(
					"FROM Student WHERE OU IS NOT null AND Topic IS '"+topic+"'" , Student.class);

			List<Student> students = query.getResultList();
			return students;

		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
		return tmp;
	}
	
	/**
	 * 	Returns a list of students in a topic
	 * @param topic - name of topic (String)
	 * @return List(Students)
	 */
	public static List<Student> getStudentsforPrediction(String topic) {
		List<Student> tmp = new LinkedList<Student>();
		try {
			TypedQuery<Student> query = entityManager.createQuery(
					"FROM Student WHERE Topic IS '"+topic+"' AND OU IS null" , Student.class);

			List<Student> students = query.getResultList();
			return students;

		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
		}
		return tmp;
	}


	/**
	 * 	Function for updating one student values in database
	 *  @param students - student object (Student)
	 *  @return id - student id
	 */
	static Long setStudent(Student students) {
		EntityTransaction transaction = entityManager.getTransaction();
		Long id = -1l;
		try {
			transaction.begin();
			if (students.ID != null && students.ID > 0) {
				entityManager.merge(students);
			} else
				entityManager.persist(students);
			transaction.commit();
			// entityManager.flush();
			id = students.ID;
			LOG.debug("Student ID:" + id);
			return id;
		} catch (Exception e) {
			LOG.error(e.getMessage() + " " + e.getCause());
			return id;
		}
	}
	
	/**
	 *  Returns student with given id
	 * 	@param id - student id
	 * 	@return student (Student)
	 */
	static Student getStudent(Long id) {
		return entityManager.find(Student.class, id);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				result.append(field.get(this));
			} catch (IllegalAccessException ex) {
				LOG.error(ex.getMessage() + " " + ex.getCause());
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}