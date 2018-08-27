/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.contactlistspringmvc.dao;

import java.util.List;
import java.util.Map;
import rs.contactlistspringmvc.model.Contact;

/**
 *
 * @author svlln
 */
public interface ContactListDao {
    
    public Contact addContact(Contact contact);
    
    public void removeContact(long contactId);
    
    public void updateContact(Contact contact);
    
    public List<Contact> getAllContacts();
    
    public Contact getContactById(long contactId);
    
    public List<Contact> searchContacts(Map<SearchTerm, String> criteria);
}
