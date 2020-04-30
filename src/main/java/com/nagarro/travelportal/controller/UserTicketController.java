package com.nagarro.travelportal.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.travelportal.Service.EmployeeService;
import com.nagarro.travelportal.Service.TicketService;
import com.nagarro.travelportal.model.Employee;
import com.nagarro.travelportal.model.Ticket;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserTicketController {

	/** The log. */
	private Logger log = Logger.getLogger(UserTicketController.class);

	/** The ticket service. */
	@Autowired
	private TicketService ticketService;

	/** The emp service. */
	@Autowired
	private EmployeeService empService;

	/**
	 * Raise ticket.
	 *
	 * @param user   the user
	 * @param ticket the ticket
	 * @return the response entity
	 */
	@PostMapping("/raiseticket")
	public ResponseEntity<String> raiseTicket(Principal principal, @Valid @RequestBody Ticket ticket) {
		String user = principal.getName();
		Employee emp = empService.getEmployeeByUsername(user);
		// foreign key set
		ticket.setEmployee(emp);
		if (ticketService.addOrUpdateTicket(ticket) != null) {
			log.info("Ticket to travel from " + ticket.getFrom() + " to " + ticket.getTravelCity()
					+ " has been raised. ");
			return new ResponseEntity<String>("Ticket Raised Succesfuly", HttpStatus.OK);
		}

		log.info("Ticket not raised ");
		return new ResponseEntity<String>("Ticket not Raised", HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Gets the ticket.
	 *
	 * @param username the username
	 * @return the ticket
	 */
	@GetMapping("/mytickets")
	public List<Ticket> getTicket(Principal principal) {
		String username = principal.getName();
		return ticketService.getByUsername(username);

	}
	
	
	/**
	 * Download file.
	 *
	 * @param id the id
	 * @return the response entity byte array
	 */
	@ResponseBody
	@GetMapping("/downloadFile/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable(value = "id") Integer id) {
		byte[] file = ticketService.getTicketById(id).getFiles();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    return new ResponseEntity<>(file, headers, HttpStatus.OK);
	}

}
