package mainpkg.ecoresort.Housekeeping;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomNumber;
    private String status;
    private String assignedTo;
    private String lastCleanedDate;
    private String nextLinenChange;

    public Room() {
    }

    public Room(int roomNumber, String status, String assignedTo, String lastCleanedDate, String nextLinenChange) {
        this.roomNumber = roomNumber;
        this.status = status;
        this.assignedTo = assignedTo;
        this.lastCleanedDate = lastCleanedDate;
        this.nextLinenChange = nextLinenChange;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getLastCleanedDate() {
        return lastCleanedDate;
    }

    public void setLastCleanedDate(String lastCleanedDate) {
        this.lastCleanedDate = lastCleanedDate;
    }

    public String getNextLinenChange() {
        return nextLinenChange;
    }

    public void setNextLinenChange(String nextLinenChange) {
        this.nextLinenChange = nextLinenChange;
    }
}
