package si.fri.rso.rsobnb.users;

import java.util.Date;

public class Ticket {

    private String id;
    private String userId;
    private String title;
    private String description;
    private Date submitted;
    private boolean solved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
