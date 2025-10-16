package dataaccess;

import datamodel.User;

public interface DataAccess {
        void clear();
        void saveUser(User user);
        User getUser(String username);
}
