package movienight.movienight.models;

import com.google.api.client.util.DateTime;

public class Calender {

    String summary;
    String createdByUser;
    DateTime startDate;
    DateTime endDate;

    public Calender() { }

    public Calender(DateTime startDate, DateTime endDate, String summary) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
    }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getCreatedByUser() { return createdByUser; }

    public void setCreatedByUser(String createdByUser) { this.createdByUser = createdByUser; }

    public DateTime getStartDate() { return startDate; }

    public void setStartDate(DateTime startDate) { this.startDate = startDate; }

    public DateTime getEndDate() { return endDate; }

    public void setEndDate(DateTime endDate) { this.endDate = endDate; }

}