/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.contactlistspringmvc.Controller;

import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.contactlistspringmvc.dao.ContactListDao;
import rs.contactlistspringmvc.model.Contact;

/**
 *
 * @author svlln
 */
@Controller
public class ContactController {
    
    ContactListDao dao;
    
    @Inject
    public ContactController(ContactListDao dao){
        this.dao = dao;
    }
    
    @RequestMapping(value = "/displayContactsPage", method = RequestMethod.GET)
    public String displayContactPage(Model model){
        
        // gets all the contacts from the dao
        List<Contact> contactList = dao.getAllContacts();
        
        // puts the list of contacts on the model
        model.addAttribute("contactList", contactList);
        
        return "contacts";
    }
    
    @RequestMapping(value = "/createContact", method = RequestMethod.POST)
    public String createContact(HttpServletRequest request){
        // grab incoming values from the form and create a new contact object
        
        Contact  contact = new Contact();
        contact.setFirstName(request.getParameter("firstName"));
        contact.setLastName(request.getParameter("lastName"));
        contact.setCompany(request.getHeader("company"));
        contact.setPhone(request.getHeader("phone"));
        contact.setEmail(request.getParameter("email"));
        
        // persist the new contact
        dao.addContact(contact);
        
        // redirect to the endpoint that displays the contact page to display contact table
        return "redirect:displayContactsPage";
        
    }
    
    @RequestMapping(value = "/displayContactDetails", method = RequestMethod.GET)
    public String displayContactDetails(HttpServletRequest request, Model model){
        String contactIdParameter = request.getParameter("contactId");
        int contactId = Integer.parseInt(contactIdParameter);
        
        Contact contact = dao.getContactById(contactId);
        
        model.addAttribute("contact", contact);
        
        return "contactDetails";
    }
    
    @RequestMapping(value = "/deleteContact" , method = RequestMethod.GET)
    public String deleteContact(HttpServletRequest request, Model model){
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        dao.removeContact(contactId);
        return "redirect:displayContactsPage";
    }
    
    @RequestMapping(value = "/displayEditContactForm", method = RequestMethod.GET)
    public String displayEditContactForm(HttpServletRequest request, Model model){
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        
        
        Contact contact = dao.getContactById(contactId);
        model.addAttribute("contact", contact);
        return "editContactForm";
    }

    // end point for edit
    @RequestMapping(value = "/editContact", method = RequestMethod.POST)
    public String editContact(@ModelAttribute("contact") Contact contact) {

        dao.updateContact(contact);

        return "redirect:displayContactsPage";
    }


}
