package mainpkg.ecoresort.Housekeeping;

import java.io.Serializable;

public class MaintenanceTicket implements Serializable {
    private String ticketId;
    private int roomNumber;
    private String issueDescription;
    private String status;
    private String reportedBy;
    private String reportedDate;

    public MaintenanceTicket() {
    }

    public MaintenanceTicket(String ticketId, int roomNumber, String issueDescription, String status, String reportedBy, String reportedDate) {
        this.ticketId = ticketId;
        this.roomNumber = roomNumber;
        this.issueDescription = issueDescription;
        this.status = status;
        this.reportedBy = reportedBy;
        this.reportedDate = reportedDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }
}
