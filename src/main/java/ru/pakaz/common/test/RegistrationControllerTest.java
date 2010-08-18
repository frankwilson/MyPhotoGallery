package ru.pakaz.common.test;

import junit.framework.TestCase;
import ru.pakaz.common.controller.RegistrationController;
import ru.pakaz.common.model.User;

public class RegistrationControllerTest extends TestCase {

	public void testSendEmail() {
		RegistrationController controller = new RegistrationController();
		
		User recipient = new User();
		recipient.setNickName( "Kolya" );
		recipient.setEmail( "pv.kazantsev@gmail.com" );
		
//		controller.sendEmailMessage(recipient);
	}
}
