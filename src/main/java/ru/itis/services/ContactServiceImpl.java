package ru.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.forms.ContactForm;
import ru.itis.models.Contact;
import ru.itis.models.User;
import ru.itis.repositories.ContactRepository;

import java.util.List;

@Component
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;
    @Override
    public void addContact(ContactForm contactForm) {
        Contact contact = Contact.builder().email(contactForm.getEmail()).
                name(contactForm.getName()).surname(contactForm.getSurname()).letter(contactForm.getLetter()).userid(contactForm.getUserid()).build();
        contactRepository.save(contact);

    }
    @Override
    public List<Contact> getAnswers(User user) {
        return contactRepository.findContactForUser(user);
    }

    @Override
    public void delete(Contact contact) {
        contactRepository.remove(contact);
    }

    @Override
    public List<Contact> getMessages() {
        return contactRepository.getMessages();
    }

    @Override
    public void addReply(Long id, String text) {
        contactRepository.addReply(id,text);
    }
}
