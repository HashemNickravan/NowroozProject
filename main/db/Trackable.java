package main.db;

import java.util.Date;

public interface Trackable {
    void setCreationDate(Date date);
    void setLastModificationDate(Date date);
    Date getCreationDate();
    Date getLastModificationDate();
}