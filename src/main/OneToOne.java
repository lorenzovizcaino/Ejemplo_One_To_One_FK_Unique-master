package main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.SessionFactoryUtil;
import modelo.ContactInfo;
import modelo.Profesor;

import java.util.List;
import java.util.Set;

public class OneToOne {
	public static void main(String[] args) {

		SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		 

		//changeContactInfoFromProfesor(session, 1);
		//changeContactInfoFromContactInfo(session, 1);
		//updateContactInfoToProfesor(session, 5);

		session.close();
		sessionFactory.close();

	}

	/*
	 * Lanza exception: OR: Infracción de la restricción UNIQUE KEY
	 * 'UC_contactInfo_UNIQUE_profesorID'. No se puede insertar una clave duplicada
	 * en el objeto 'dbo.contactInfo'. El valor de la clave duplicada es (1).
	 * No se elimina el contactInfo inicialmente asociado al profesor y se intenta crear otro con el mismo profesorId
	 */
	private static void changeContactInfoFromProfesor(Session session, int profeId) {
		Transaction tx = null;

		Profesor profe = (Profesor) session.createQuery("SELECT p FROM Profesor p where p.id = :id")
				.setParameter("id", profeId).uniqueResult();

		ContactInfo info = profe.getContactInfo();

		System.out.println("Profe: " + profe.getId() + " Contact info: " + info);

		ContactInfo cInfoNueva = new ContactInfo();
		cInfoNueva.setEmail("algo@algo.com");
		cInfoNueva.setTlfMovil("666 123 123");

		// Relación bidireccional
		profe.addContactInfo(cInfoNueva);

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(cInfoNueva);
			session.saveOrUpdate(profe);

			tx.commit();
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}
	}
	/*
	 * Lanza exception: OR: Infracción de la restricción UNIQUE KEY
	 * 'UC_contactInfo_UNIQUE_profesorID'. No se puede insertar una clave duplicada
	 * en el objeto 'dbo.contactInfo'. El valor de la clave duplicada es (1).
	 * No se elimina el contactInfo inicialmente asociado al profesor y se intenta crear otro con el mismo profesorId
	 */
	private static void changeContactInfoFromContactInfo(Session session, int profeId) {

		ContactInfo info = (ContactInfo)
		// session.createQuery("SELECT p.contactInfos from Profesor p where p.id = :id")
		session.createQuery("SELECT c from ContactInfo c where c.profesor.id = :id").setParameter("id", profeId)
				.uniqueResult();

		ContactInfo cInfoNueva = new ContactInfo();
		cInfoNueva.setEmail("algo2@algo.com");
		cInfoNueva.setTlfMovil("666 123 123");
		Transaction tx = null;
		try {

			tx = session.beginTransaction();

			System.out.println("contact info: " + info + " profesor: " + info.getProfesor());
			Profesor profe = info.getProfesor();

			// Relación bidireccional
			profe.addContactInfo(cInfoNueva);

			/*
			 * ERROR: Infracción de la restricción UNIQUE KEY
			 * 'UC_contactInfo_UNIQUE_profesorID'. No se puede insertar una clave duplicada
			 * en el objeto 'dbo.contactInfo'. El valor de la clave duplicada es (1).
			 */
			session.saveOrUpdate(cInfoNueva);
			session.saveOrUpdate(profe);

			tx.commit();
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
			if (tx != null) {
				tx.rollback();
			}
		}

	}
	
	private static void updateContactInfoToProfesor(Session session, int profeId) {
		Transaction tx = null;

		Profesor profe = (Profesor) session.createQuery("SELECT p FROM Profesor p where p.id = :id")
				.setParameter("id", profeId).uniqueResult();

		//info está asociado a la sesión con pk=1
		ContactInfo info =  profe.getContactInfo();
		if(info!=null) {
			System.out.println("Profe: " + profe.getId() + " Contact info: " + info);
		}
		

	
		info.setEmail("algo@algo2.com");
		info.setTlfMovil("666 123 123");

		profe.addContactInfo(info);

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(profe);
			session.saveOrUpdate(info);

			tx.commit();
			System.out.println("Se ha actualizado correctamente");
		} catch (Exception ex) {
			System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
			ex.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw ex;
		}
	}


}
