package ru.itis.services;

import ru.itis.forms.ContactForm;
import ru.itis.models.Contact;
import ru.itis.models.User;

import java.util.List;

public interface ContactService  {
    void addContact(ContactForm contactForm);
    List<Contact> getAnswers(User user);
    void delete(Contact contact);
    List<Contact> getMessages();
    void addReply(Long id, String text);
}
