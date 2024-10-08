package br.com.alura.ProjetoAlura.registration;

public interface RegistrationReportProjection {

    String getCourseName();
    String getCourseCode();
    String getInstructorName();
    String getInstructorEmail();
    Long getTotalRegistrations();
}
