/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.contactlistspringmvc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import rs.contactlistspringmvc.model.Contact;

/**
 *
 * @author svlln
 */
public class ContactListInMemImpl implements ContactListDao{

    
    // going to hold all of our contact objects - simulates data base
    
    private Map<Long, Contact> contactMap = new HashMap<>();
    // used to assign ids to contacts
    // primary keys fot the contact in the database
    private static long contactIdCounter = 0;
    
    @Override
    public Contact addContact(Contact contact) {
        // assign the current counter values as the contactId
        contact.setContactId(contactIdCounter);
        contactIdCounter++;
        contactMap.put(contact.getContactId(), contact);
        return contact;
    }

    @Override
    public void removeContact(long contactId) {
        contactMap.remove(contactId);
    }

    @Override
    public void updateContact(Contact contact) {
        contactMap.put(contact.getContactId(), contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        Collection<Contact> c = contactMap.values();
        return new ArrayList(c);
    }

    @Override
    public Contact getContactById(long contactId) {
         return contactMap.get(contactId);
    }

    @Override
    public List<Contact> searchContacts(Map<SearchTerm, String> criteria) {
        // Get all the search values from the map
        String firstNameSearchCriteria =
                criteria.get(SearchTerm.FIRST_NAME);
        String lastNameSearchCriteria =
                criteria.get(SearchTerm.LAST_NAME);
        String companySearchCriteria =
                criteria.get(SearchTerm.COMPANY);
        String phoneSearchCriteria =
                criteria.get(SearchTerm.PHONE);
        String emailSearchCriteria =
                criteria.get(SearchTerm.EMAIL);
        
        // Declare all the predicate conditions - returns boolean
        Predicate<Contact> firstNameMatchPredicate;
        Predicate<Contact> lastNameMatchPredicate;
        Predicate<Contact> companyMatchPredicate;
        Predicate<Contact> phoneMatchPredicate;
        Predicate<Contact> emailMatchPredicate;
        
        Predicate<Contact> truePredicate = (c) -> {
            return true;
        };
        
        // Assign value to predicates, if a given search is empty just assign the default truePredicate, otherwise assign the predicate that only returns true whrn it finds a match to the given term
        if(firstNameSearchCriteria == null ||
            firstNameSearchCriteria.isEmpty()){
            firstNameMatchPredicate = truePredicate;
        } else{
            firstNameMatchPredicate =
                    (c) -> c.getFirstName().equalsIgnoreCase(firstNameSearchCriteria);
        }
        if(lastNameSearchCriteria == null ||
                lastNameSearchCriteria.isEmpty()){
            lastNameMatchPredicate = truePredicate;
        } else {
            lastNameMatchPredicate =
                    (c) -> c.getLastName().equalsIgnoreCase(lastNameSearchCriteria);
        }
        if (companySearchCriteria == null
                || companySearchCriteria.isEmpty()) {
            companyMatchPredicate = truePredicate;
        } else {
            companyMatchPredicate
                    = (c) -> c.getCompany().equalsIgnoreCase(companySearchCriteria);
        }
        if (phoneSearchCriteria == null
                || phoneSearchCriteria.isEmpty()) {
            phoneMatchPredicate = truePredicate;
        } else {
            phoneMatchPredicate
                    =    (c) -> c.getPhone().equalsIgnoreCase(phoneSearchCriteria);
        }
        if (emailSearchCriteria == null
                || emailSearchCriteria.isEmpty()) {
            emailMatchPredicate = truePredicate;
        } else {
            emailMatchPredicate
                    = (c) -> c.getEmail().equalsIgnoreCase(emailSearchCriteria);
        }
       
        // returns the list of contacts that match the given criteria
        
        return contactMap.values().stream()
                .filter(firstNameMatchPredicate
                        .and(lastNameMatchPredicate)
                        .and(companyMatchPredicate)
                        .and(phoneMatchPredicate)
                        .and(emailMatchPredicate))
                
                .collect(Collectors.toList());
    
    }
    
}
