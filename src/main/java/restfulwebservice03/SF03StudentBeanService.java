package restfulwebservice03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SF03StudentBeanService {

	private SF03StudentBeanRepository studentRepo;
    
	@Autowired
	public SF03StudentBeanService(SF03StudentBeanRepository studentRepo) {

		this.studentRepo = studentRepo;
	}
	
	
	public List<SF03StudentBean> listStudents(){
		
		return studentRepo.findAll();
		
	}
	
	
	
	
	
	
	
	public SF03StudentBean selectStdById(long id){
		
	//	return studentRepo.findById(id).get();
		if(studentRepo.findById(id).isPresent()) {
			
			return studentRepo.findById(id).get();
		}
		else {
			
			return new SF03StudentBean();
		}
		
	}
	
	 public String deleteStdById(Long id) {
		 
		 if(studentRepo.findById(id).isPresent()) {
			 
			 studentRepo.deleteById(id);
			 return "data was deleted whose id is "+id;		
			 }
		 
	 
	 else{
		 return "there is no any student with that "+id;
		 
	 }
	

	 }	
	 
	 
	 
	 //that code for update data specilay for name
	 public SF03StudentBean updateStudent(Long id,SF03StudentBean newStudent) {
		 
		 
		 SF03StudentBean existingStdById=studentRepo.findById(id).
				 orElseThrow(()->new IllegalStateException("this student does not exist"));
		 
		 String name=existingStdById.getName();
		 
		 if(newStudent.getName()==null) {
			 existingStdById.setName(null);
		 }
		 else if(existingStdById.getName()==null) {
			 existingStdById.setName(newStudent.getName());
		 } else if(!name.equals(newStudent.getName())) {
			 existingStdById.setName(newStudent.getName());
			 
		 }
		 
	/// that code for update email part creterias	 
  Optional<SF03StudentBean> existingEmailStdById=studentRepo.findSF03StudentBeanByEmail(newStudent.getEmail());
		 
		 if(existingEmailStdById.isPresent()) {
			 
			 
			 throw new IllegalStateException("this email already taken");
		 }
		 else if(newStudent.getEmail()==null) {
			 
			 throw new IllegalArgumentException("email cannot be empty");
			 
		 }else if(!newStudent.getEmail().contains("@")) {
			 throw new IllegalArgumentException("there is no email like that ");
		 }else {
			 existingStdById.setEmail(newStudent.getEmail());
		 }
		 
		 //that about DOB
		 if(Period.between(newStudent.getDob(), LocalDate.now()).isNegative()) {
			 
			 throw new IllegalStateException("that date not available for DOB");
			 
		 }
		 else if(!existingStdById.getDob().equals(newStudent.getDob())) {
			 existingStdById.setDob(newStudent.getDob());
			 
		 }
		 
		 
		 
		 return studentRepo.save(existingStdById);
		 
	 }
	//This method is for partial update
		public SF03StudentBean updateStdPartially(Long id, SF03StudentBean newStudent) {
			
			SF03StudentBean existingStudentById = studentRepo
														.findById(id)
														.orElseThrow(()->new IllegalStateException(id + " id does not exist.."));
			
			if(newStudent.getName()!=null) {
				existingStudentById.setName(newStudent.getName());
			}
			
			if(newStudent.getEmail()==null) {
				newStudent.setEmail("");
			}
			
			Optional<SF03StudentBean> existingStudentByEmail = studentRepo.findSF03StudentBeanByEmail(newStudent.getEmail());
			if(existingStudentByEmail.isPresent()) {
				throw new IllegalStateException("Email exists in DB, email must be unique...");
			}else if(!newStudent.getEmail().contains("@") && newStudent.getEmail()!="") {
				throw new IllegalStateException("Invalid email...");
			}else if(newStudent.getEmail()!="") {
				existingStudentById.setEmail(newStudent.getEmail());
			}
			
			if(newStudent.getDob()!=null) {
				existingStudentById.setDob(newStudent.getDob());
			}
			
			existingStudentById.setAge(newStudent.getAge());
			existingStudentById.setErrMsg("No error...");
			
			return studentRepo.save(existingStudentById);
		}
	 
	 
	 
		//This method is for adding new student into DB
		public SF03StudentBean addStudent(SF03StudentBean newStudent) throws ClassNotFoundException, SQLException {
			
			Optional<SF03StudentBean> existingStudentByEmail = studentRepo.findSF03StudentBeanByEmail(newStudent.getEmail());
			
			if(existingStudentByEmail.isPresent()) {
				throw new IllegalStateException("Email exists in DB, email must be unique...");
			}
			if(newStudent.getName()==null) {
				throw new IllegalStateException("Name must be entered for new students...");
			}
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", "hr", "oracle");
			
			Statement st = con.createStatement();
			
			String sql = "SELECT max(id) FROM students";
			ResultSet result = st.executeQuery(sql);
			
			Long maxId = 0L;
			
			while(result.next()) {
				maxId = result.getLong(1);
			}
			
			newStudent.setId(maxId + 1);
			newStudent.setAge(newStudent.getAge());
			newStudent.setErrMsg("No error...");
			
			return studentRepo.save(newStudent);
		}
		
	}
