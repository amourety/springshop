package ru.itis.repositories;

import ru.itis.models.Contact;
import ru.itis.models.User;

import java.util.List;

public interface ContactRepository extends CrudRepository <Contact>
{
    List<Contact> findContactForUser(User user);
    void remove(Contact contact);
    List<Contact> getMessages();
    void addReply(Long id, String text);
}
